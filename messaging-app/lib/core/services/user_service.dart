// lib/core/services/user_service.dart
import 'dart:convert';
import 'package:http/http.dart' as http;
import '../../data/models/user_model.dart';
import '../constants/api_constants.dart';
import '../storage/secure_storage.dart';

class UserService {
  static Future<Map<String, String>> _authHeaders() async {
    final token = await SecureStorage.getToken();
    return {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    };
  }

  /// Lista todos los usuarios registrados
  static Future<List<UserModel>> getUsers() async {
    final response = await http.get(
      Uri.parse(ApiConstants.users),
      headers: await _authHeaders(),
    );

    if (response.statusCode == 200) {
      final List data = jsonDecode(response.body);
      return data.map((json) => UserModel.fromJson(json)).toList();
    } else {
      throw Exception('Error cargando usuarios');
    }
  }

  /// Obtiene el perfil de un usuario por email
  static Future<UserModel> getUserByEmail(String email) async {
    final encoded = Uri.encodeComponent(email);
    final response = await http.get(
      Uri.parse('${ApiConstants.users}/$encoded'),
      headers: await _authHeaders(),
    );

    if (response.statusCode == 200) {
      return UserModel.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Usuario no encontrado');
    }
  }
}
