// lib/data/models/user_model.dart

class UserModel {
  final String email;
  final String fullName;
  final String phoneNumber;
  final String role;
  final String? photoUrl;

  UserModel({
    required this.email,
    required this.fullName,
    required this.phoneNumber,
    required this.role,
    this.photoUrl,
  });

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
      email:       json['email'] ?? '',
      fullName:    json['fullName'] ?? '',
      phoneNumber: json['phoneNumber'] ?? '',
      role:        json['role'] ?? '',
      photoUrl:    json['photoUrl'],
    );
  }
}
