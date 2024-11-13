    package com.example.santeconnect.Activity.DAO;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.util.Base64;

    import com.example.santeconnect.Activity.Database.DatabaseHelper;
    import com.example.santeconnect.Activity.Entities.User;

    import java.util.ArrayList;
    import java.util.List;

    import javax.crypto.Cipher;
    import javax.crypto.spec.SecretKeySpec;

    public class UserDAO {

        private static final String TABLE_NAME = "users";
        private static final String COL_ID = "ID";
        private static final String COL_NAME = "NAME";
        private static final String COL_EMAIL = "EMAIL";
        private static final String COL_ROLE = "ROLE";
        private static final String COL_PASSWORD = "PASSWORD";
        private static final String COL_RESET_PASSWORD = "RESET_CODE";
        private static final String COL_RESET_EXPIRY = "RESET_EXPIRY";
        private static final String AES = "AES";
        private static final byte[] encryptionKey = "1234567890123456".getBytes();

        private final SQLiteDatabase database;
        private final DatabaseHelper dbHelper;

        public UserDAO(Context context) {
            dbHelper = new DatabaseHelper(context);
            database = dbHelper.getWritableDatabase();
        }

        // Encryption method
        private String encrypt(String data) throws Exception {
            SecretKeySpec key = new SecretKeySpec(encryptionKey, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encryptedData, Base64.DEFAULT);
        }
        public boolean updateUser(User user) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_NAME, user.getName());
            values.put(COL_EMAIL, user.getEmail());
            values.put("PROFILE_IMAGE_URI", user.getProfileImageUri());  // Add this line to update the image URI

            int rowsAffected = db.update(TABLE_NAME, values, COL_ID + "=?", new String[]{String.valueOf(user.getId())});
            return rowsAffected > 0;
        }
        // Decryption method
        private String decrypt(String data) throws Exception {
            SecretKeySpec key = new SecretKeySpec(encryptionKey, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedData = Base64.decode(data, Base64.DEFAULT);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData);
        }

        // Add this method to the UserDAO class
        public User getUserByEmailAndPassword(String email, String password) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME,
                    new String[]{COL_ID, COL_NAME, COL_EMAIL, COL_ROLE, COL_PASSWORD},
                    COL_EMAIL + "=?", new String[]{email},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                try {
                    // Retrieve and decrypt the stored password
                    String storedEncryptedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD));
                    String decryptedPassword = decrypt(storedEncryptedPassword);

                    // Compare with the input password
                    if (decryptedPassword.equals(password)) {
                        // Create and populate User object with database values
                        User user = new User();
                        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                        user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
                        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
                        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
                        //user.setProfileImageUri(cursor.getString(cursor.getColumnIndexOrThrow(COL_PROFILE_IMAGE_URI)));
                        cursor.close();
                        return user;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (cursor != null) cursor.close();
            return null;
        }

        public boolean checkUserExists(String email) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COL_ID},
                    COL_EMAIL + "=?", new String[]{email},
                    null, null, null);
            boolean exists = (cursor.getCount() > 0);
            cursor.close();
            return exists;
        }
        public User getUserById(int userId) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME,
                    new String[]{COL_ID, COL_NAME, COL_EMAIL, COL_ROLE, "PROFILE_IMAGE_URI"}, // Added PROFILE_IMAGE_URI
                    COL_ID + "=?",
                    new String[]{String.valueOf(userId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
                user.setProfileImageUri(cursor.getString(cursor.getColumnIndexOrThrow("PROFILE_IMAGE_URI"))); // Added this line
                cursor.close();
                return user;
            }

            if (cursor != null) cursor.close();
            return null;
        }


        public boolean insertUser(User user) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                // Encrypt the password
                String encryptedPassword = encrypt(user.getPassword());

                // Prepare the SQL statement with the profile image URI
                String sql = "INSERT INTO " + TABLE_NAME + " ("
                        + COL_NAME + ", "
                        + COL_EMAIL + ", "
                        + COL_PASSWORD + ", "
                        + COL_ROLE +") VALUES (?, ?, ?, ?)";

                // Execute the SQL statement
                db.execSQL(sql, new Object[]{
                        user.getName(),
                        user.getEmail(),
                        encryptedPassword,
                        user.getRole(),
                      //  user.getProfileImageUri()
                });

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        public boolean updateResetCode(String email, String resetCode, long expiryTime) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_RESET_PASSWORD, resetCode);
            values.put(COL_RESET_EXPIRY, expiryTime);

            int rowsAffected = db.update(TABLE_NAME, values,
                    COL_EMAIL + " = ?", new String[]{email});
            return rowsAffected > 0;
        }

        public boolean updatePassword(String email, String newPassword) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                // Encrypt the new password
                String encryptedNewPassword = encrypt(newPassword);

                ContentValues values = new ContentValues();
                values.put(COL_PASSWORD, encryptedNewPassword);
                values.put(COL_RESET_PASSWORD, (String) null);
                values.put(COL_RESET_EXPIRY, 0); // Clear expiry time

                int rowsAffected = db.update(TABLE_NAME, values,
                        COL_EMAIL + " = ?", new String[]{email});
                return rowsAffected > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean verifyResetCode(String email, String resetCode) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(TABLE_NAME,
                    new String[]{COL_RESET_PASSWORD, COL_RESET_EXPIRY},
                    COL_EMAIL + " = ?", new String[]{email},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String storedCode = cursor.getString(0);
                long expiryTime = cursor.getLong(1);
                cursor.close();

                // Code must match and current time must be before expiry time
                return storedCode != null &&
                        storedCode.equals(resetCode) &&
                        System.currentTimeMillis() < expiryTime;
            }
            cursor.close();
            return false;
        }


        public boolean checkUser(String email, String password) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COL_PASSWORD},
                    COL_EMAIL + "=?", new String[]{email},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                try {
                    // Decrypt stored password and compare with input password
                    String storedPassword = decrypt(cursor.getString(0));
                    cursor.close();
                    return storedPassword.equals(password);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if (cursor != null) cursor.close();
            return false;
        }

        public int getUserIdByEmail(String email) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME,
                    new String[]{COL_ID},
                    COL_EMAIL + "=?",
                    new String[]{email},
                    null, null, null);

            int userId = -1;
            if (cursor != null && cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                cursor.close();
            }
            return userId;
        }
        public List<User> getDoctors() {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            List<User> doctorList = new ArrayList<>();

            Cursor cursor = db.query(TABLE_NAME,
                    new String[]{COL_ID, COL_NAME, COL_EMAIL, COL_ROLE},
                    COL_ROLE + "=?",
                    new String[]{"doctor"},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                    user.setName(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
                    user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)));
                    user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE)));
                    doctorList.add(user);
                } while (cursor.moveToNext());
                cursor.close();
            }

            return doctorList;
        }



    }
