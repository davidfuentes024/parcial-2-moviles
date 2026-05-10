// lib/core/services/message_service.dart
import 'dart:convert';
import 'package:http/http.dart' as http;
import '../../data/models/message_model.dart';
import '../constants/api_constants.dart';
import '../storage/secure_storage.dart';

class MessageService {
  static Future<Map<String, String>> _authHeaders() async {
    final token = await SecureStorage.getToken();
    return {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    };
  }

  /// Envía un mensaje a otro usuario (dispara push notification en el backend)
  static Future<void> sendMessage({
    required String recipientEmail,
    required String title,
    required String body,
  }) async {
    final response = await http.post(
      Uri.parse(ApiConstants.sendMessage),
      headers: await _authHeaders(),
      body: jsonEncode({
        'recipientEmail': recipientEmail,
        'title':          title,
        'body':           body,
      }),
    );

    if (response.statusCode != 200) {
      final data = jsonDecode(response.body);
      throw Exception(data['error'] ?? 'Error al enviar el mensaje');
    }
  }

  /// Obtiene los mensajes recibidos del usuario autenticado
  static Future<List<MessageModel>> getReceivedMessages() async {
    final response = await http.get(
      Uri.parse(ApiConstants.receivedMessages),
      headers: await _authHeaders(),
    );

    if (response.statusCode == 200) {
      final List data = jsonDecode(response.body);
      return data.map((json) => MessageModel.fromJson(json)).toList();
    } else {
      throw Exception('Error cargando mensajes');
    }
  }
}
