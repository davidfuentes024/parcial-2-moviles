// lib/data/models/message_model.dart

class MessageModel {
  final String id;
  final String title;
  final String body;
  final String senderEmail;
  final String? senderName;
  final String? senderPhotoUrl;
  final String recipientEmail;
  final DateTime sentAt;

  MessageModel({
    required this.id,
    required this.title,
    required this.body,
    required this.senderEmail,
    this.senderName,
    this.senderPhotoUrl,
    required this.recipientEmail,
    required this.sentAt,
  });

  factory MessageModel.fromJson(Map<String, dynamic> json) {
    return MessageModel(
      id:             json['id'] ?? '',
      title:          json['title'] ?? '',
      body:           json['body'] ?? '',
      senderEmail:    json['senderEmail'] ?? '',
      senderName:     json['senderName'],
      senderPhotoUrl: json['senderPhotoUrl'],
      recipientEmail: json['recipientEmail'] ?? '',
      sentAt: DateTime.tryParse(json['sentAt'] ?? '') ?? DateTime.now(),
    );
  }
}
