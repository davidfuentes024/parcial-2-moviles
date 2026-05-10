// lib/core/constants/api_constants.dart

class ApiConstants {
  // ─────────────────────────────────────────────────────────────────────────
  // EMULADOR Android → usa 10.0.2.2 (apunta al localhost de tu Mac/PC)
  // DISPOSITIVO FÍSICO → cambia a la IP local de tu Mac, ej: 192.168.1.105
  //   En Mac: abre Terminal y ejecuta: ifconfig | grep "inet "
  // ─────────────────────────────────────────────────────────────────────────
  static const String baseUrl = 'http://10.0.2.2:8080/api';

  // Auth
  static const String register = '$baseUrl/auth/register';
  static const String login    = '$baseUrl/auth/login';

  // Users
  static const String users    = '$baseUrl/users';

  // Messages
  static const String sendMessage      = '$baseUrl/messages/send';
  static const String receivedMessages = '$baseUrl/messages/received';
}
