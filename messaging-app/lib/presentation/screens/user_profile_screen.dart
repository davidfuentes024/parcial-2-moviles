// lib/presentation/screens/user_profile_screen.dart
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import '../../core/services/message_service.dart';
import '../../data/models/user_model.dart';

class UserProfileScreen extends StatelessWidget {
  final UserModel user;

  const UserProfileScreen({super.key, required this.user});

  void _openSendMessageModal(BuildContext context) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (_) => _SendMessageModal(recipient: user),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Perfil de usuario')),
      body: SingleChildScrollView(
        child: Column(
          children: [
            // ── Cabecera con foto ────────────────────────────────────────
            Container(
              width: double.infinity,
              decoration: const BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                  colors: [Color(0xFF1565C0), Color(0xFF42A5F5)],
                ),
              ),
              padding: const EdgeInsets.symmetric(vertical: 32),
              child: Column(
                children: [
                  // Foto
                  Container(
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      border: Border.all(color: Colors.white, width: 3),
                    ),
                    child: CircleAvatar(
                      radius: 56,
                      backgroundColor: Colors.grey.shade200,
                      child: ClipOval(
                        child: user.photoUrl != null && user.photoUrl!.isNotEmpty
                            ? CachedNetworkImage(
                                imageUrl: user.photoUrl!,
                                width: 112,
                                height: 112,
                                fit: BoxFit.cover,
                                errorWidget: (_, __, ___) => _avatarFallback(),
                              )
                            : _avatarFallback(),
                      ),
                    ),
                  ),
                  const SizedBox(height: 12),
                  Text(
                    user.fullName,
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Container(
                    padding:
                        const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
                    decoration: BoxDecoration(
                      color: Colors.white24,
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: Text(
                      user.role,
                      style: const TextStyle(color: Colors.white, fontSize: 13),
                    ),
                  ),
                ],
              ),
            ),

            // ── Atributos del usuario ────────────────────────────────────
            Padding(
              padding: const EdgeInsets.all(20),
              child: Column(
                children: [
                  _InfoCard(
                    items: [
                      _InfoItem(icon: Icons.email_outlined,    label: 'Email',    value: user.email),
                      _InfoItem(icon: Icons.phone_outlined,    label: 'Teléfono', value: user.phoneNumber),
                      _InfoItem(icon: Icons.work_outline,      label: 'Cargo',    value: user.role),
                    ],
                  ),
                  const SizedBox(height: 24),

                  // ── Botón enviar mensaje ─────────────────────────────
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton.icon(
                      icon: const Icon(Icons.send_rounded),
                      label: const Text('Enviar mensaje',
                          style: TextStyle(fontSize: 16)),
                      onPressed: () => _openSendMessageModal(context),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _avatarFallback() {
    final initials = user.fullName.trim().isEmpty
        ? '?'
        : user.fullName.trim().split(' ').map((w) => w[0]).take(2).join().toUpperCase();
    return Container(
      width: 112,
      height: 112,
      color: const Color(0xFF1565C0),
      child: Center(
        child: Text(initials,
            style: const TextStyle(
                color: Colors.white, fontSize: 36, fontWeight: FontWeight.bold)),
      ),
    );
  }
}

// ─── Card de atributos ─────────────────────────────────────────────────────

class _InfoCard extends StatelessWidget {
  final List<_InfoItem> items;
  const _InfoCard({required this.items});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 8),
        child: Column(
          children: items
              .map((item) => ListTile(
                    leading: Icon(item.icon, color: const Color(0xFF1565C0)),
                    title: Text(item.label,
                        style: const TextStyle(
                            fontSize: 12, color: Colors.grey)),
                    subtitle: Text(item.value,
                        style: const TextStyle(
                            fontSize: 15, fontWeight: FontWeight.w500)),
                  ))
              .toList(),
        ),
      ),
    );
  }
}

class _InfoItem {
  final IconData icon;
  final String label;
  final String value;
  const _InfoItem({required this.icon, required this.label, required this.value});
}

// ─── Modal para enviar mensaje ─────────────────────────────────────────────

class _SendMessageModal extends StatefulWidget {
  final UserModel recipient;
  const _SendMessageModal({required this.recipient});

  @override
  State<_SendMessageModal> createState() => _SendMessageModalState();
}

class _SendMessageModalState extends State<_SendMessageModal> {
  final _formKey  = GlobalKey<FormState>();
  final _titleCtrl = TextEditingController();
  final _bodyCtrl  = TextEditingController();
  bool  _loading   = false;

  Future<void> _send() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() => _loading = true);

    try {
      await MessageService.sendMessage(
        recipientEmail: widget.recipient.email,
        title:          _titleCtrl.text.trim(),
        body:           _bodyCtrl.text.trim(),
      );

      if (!mounted) return;
      Navigator.of(context).pop();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Mensaje enviado a ${widget.recipient.fullName}'),
          backgroundColor: Colors.green,
        ),
      );
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(e.toString().replaceFirst('Exception: ', ''))),
      );
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom),
      child: Container(
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
        ),
        padding: const EdgeInsets.all(24),
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  const Icon(Icons.send_rounded, color: Color(0xFF1565C0)),
                  const SizedBox(width: 8),
                  Text(
                    'Mensaje para ${widget.recipient.fullName}',
                    style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _titleCtrl,
                decoration: const InputDecoration(
                  labelText: 'Título',
                  prefixIcon: Icon(Icons.title),
                ),
                validator: (v) => v == null || v.trim().isEmpty ? 'Obligatorio' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _bodyCtrl,
                maxLines: 3,
                decoration: const InputDecoration(
                  labelText: 'Mensaje',
                  prefixIcon: Icon(Icons.message_outlined),
                  alignLabelWithHint: true,
                ),
                validator: (v) => v == null || v.trim().isEmpty ? 'Obligatorio' : null,
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: _loading ? null : _send,
                  child: _loading
                      ? const SizedBox(
                          height: 20,
                          width: 20,
                          child: CircularProgressIndicator(strokeWidth: 2, color: Colors.white),
                        )
                      : const Text('Enviar', style: TextStyle(fontSize: 16)),
                ),
              ),
              const SizedBox(height: 8),
            ],
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    _titleCtrl.dispose();
    _bodyCtrl.dispose();
    super.dispose();
  }
}
