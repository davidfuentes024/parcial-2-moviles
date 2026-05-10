// lib/core/storage/secure_storage.dart
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class SecureStorage {
  static const _storage = FlutterSecureStorage();
  static const _keyToken     = 'jwt_token';
  static const _keyEmail     = 'user_email';
  static const _keyName      = 'user_name';
  static const _keyPhotoUrl  = 'user_photo_url';

  // ─── JWT ─────────────────────────────────────────────────────────────────

  static Future<void> saveToken(String token) =>
      _storage.write(key: _keyToken, value: token);

  static Future<String?> getToken() =>
      _storage.read(key: _keyToken);

  static Future<void> deleteToken() =>
      _storage.delete(key: _keyToken);

  // ─── Datos del usuario autenticado ───────────────────────────────────────

  static Future<void> saveUserData({
    required String email,
    required String name,
    String? photoUrl,
  }) async {
    await _storage.write(key: _keyEmail, value: email);
    await _storage.write(key: _keyName, value: name);
    await _storage.write(key: _keyPhotoUrl, value: photoUrl ?? '');
  }

  static Future<String?> getEmail() => _storage.read(key: _keyEmail);
  static Future<String?> getName()  => _storage.read(key: _keyName);
  static Future<String?> getPhotoUrl() => _storage.read(key: _keyPhotoUrl);

  static Future<bool> isLoggedIn() async {
    final token = await getToken();
    return token != null && token.isNotEmpty;
  }

  static Future<void> clearAll() => _storage.deleteAll();
}
