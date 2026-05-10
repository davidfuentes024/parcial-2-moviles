// lib/core/services/auth_service.dart
import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart';
import '../constants/api_constants.dart';
import '../storage/secure_storage.dart';

class AuthService {
  /// Registra un nuevo usuario enviando multipart/form-data
  static Future<Map<String, dynamic>> register({
    required String email,
    required String password,
    required String fullName,
    required String phoneNumber,
    required String role,
    required String fcmToken,
    File? photo,
  }) async {
    final request = http.MultipartRequest('POST', Uri.parse(ApiConstants.register));

    request.fields['email']       = email;
    request.fields['password']    = password;
    request.fields['fullName']    = fullName;
    request.fields['phoneNumber'] = phoneNumber;
    request.fields['role']        = role;
    request.fields['fcmToken']    = fcmToken;

    if (photo != null) {
      final extension = photo.path.split('.').last.toLowerCase();
      final mimeType = extension == 'png' ? 'image/png' : 'image/jpeg';
      request.files.add(await http.MultipartFile.fromPath(
        'photo',
        photo.path,
        contentType: MediaType.parse(mimeType),
      ));
    }

    final streamedResponse = await request.send();
    final response = await http.Response.fromStream(streamedResponse);
    final data = jsonDecode(response.body) as Map<String, dynamic>;

    if (response.statusCode == 200) {
      await SecureStorage.saveToken(data['token']);
      await SecureStorage.saveUserData(
        email:    data['email'],
        name:     data['fullName'],
        photoUrl: data['photoUrl'],
      );
      return data;
    } else {
      throw Exception(data['error'] ?? 'Error al registrar usuario');
    }
  }

  /// Inicia sesión con email, contraseña y token FCM
  static Future<Map<String, dynamic>> login({
    required String email,
    required String password,
    required String fcmToken,
  }) async {
    final response = await http.post(
      Uri.parse(ApiConstants.login),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'email':    email,
        'password': password,
        'fcmToken': fcmToken,
      }),
    );

    final data = jsonDecode(response.body) as Map<String, dynamic>;

    if (response.statusCode == 200) {
      await SecureStorage.saveToken(data['token']);
      await SecureStorage.saveUserData(
        email:    data['email'],
        name:     data['fullName'],
        photoUrl: data['photoUrl'],
      );
      return data;
    } else {
      throw Exception(data['error'] ?? 'Email o contraseña incorrectos');
    }
  }

  /// Cierra sesión limpiando datos locales
  static Future<void> logout() async {
    await SecureStorage.clearAll();
  }
}
