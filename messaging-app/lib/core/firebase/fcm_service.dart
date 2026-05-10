// lib/core/firebase/fcm_service.dart
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';

/// Maneja Firebase Cloud Messaging para Android.
/// Android NO requiere cuenta de Apple Developer ni pagos.

// Handler para mensajes en background (debe ser función top-level)
@pragma('vm:entry-point')
Future<void> firebaseMessagingBackgroundHandler(RemoteMessage message) async {
  // El mensaje llegó con la app cerrada o en background
  // Flutter lo maneja automáticamente mostrando la notificación
}

class FcmService {
  static final _messaging = FirebaseMessaging.instance;

  static final _localNotifications = FlutterLocalNotificationsPlugin();

  static const _androidChannel = AndroidNotificationChannel(
    'messaging_channel',
    'Mensajes',
    description: 'Notificaciones de mensajes recibidos',
    importance: Importance.high,
  );

  /// Inicializa FCM y retorna el token del dispositivo.
  static Future<String?> initialize() async {
    // En Android no se necesita pedir permisos explícitamente
    // (en Android 13+ se pide automáticamente)
    await _messaging.requestPermission();

    // Configurar canal de notificaciones para Android 8+
    await _localNotifications
        .resolvePlatformSpecificImplementation<
            AndroidFlutterLocalNotificationsPlugin>()
        ?.createNotificationChannel(_androidChannel);

    // Registrar handler de background
    FirebaseMessaging.onBackgroundMessage(firebaseMessagingBackgroundHandler);

    // Manejar notificaciones en foreground (app abierta)
    FirebaseMessaging.onMessage.listen(_handleForegroundMessage);

    // Obtener y retornar el token FCM del dispositivo
    final token = await _messaging.getToken();
    return token;
  }

  /// Muestra la notificación localmente cuando la app está en primer plano
  static void _handleForegroundMessage(RemoteMessage message) {
    final notification = message.notification;
    if (notification == null) return;

    _localNotifications.show(
      notification.hashCode,
      notification.title,
      notification.body,
      NotificationDetails(
        android: AndroidNotificationDetails(
          _androidChannel.id,
          _androidChannel.name,
          channelDescription: _androidChannel.description,
          importance: Importance.high,
          priority: Priority.high,
        ),
      ),
    );
  }
}
