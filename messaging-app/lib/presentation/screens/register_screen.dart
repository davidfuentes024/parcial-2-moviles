// lib/presentation/screens/register_screen.dart
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import '../../core/firebase/fcm_service.dart';
import '../../core/services/auth_service.dart';
import 'users_screen.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final _formKey       = GlobalKey<FormState>();
  final _emailCtrl     = TextEditingController();
  final _passCtrl      = TextEditingController();
  final _nameCtrl      = TextEditingController();
  final _phoneCtrl     = TextEditingController();
  final _roleCtrl      = TextEditingController();

  File?   _photoFile;
  bool    _loading     = false;
  bool    _obscurePass = true;

  // Roles de ejemplo según el enunciado
  final List<String> _roleOptions = [
    'Auxiliar', 'Técnico redes', 'Servicios generales',
    'Operador logístico', 'Contador', 'Subgerente', 'Otro',
  ];
  String? _selectedRole;

  Future<void> _pickPhoto() async {
    final picker = ImagePicker();
    final picked = await picker.pickImage(
      source: ImageSource.gallery,
      maxWidth: 800,
      imageQuality: 80,
    );
    if (picked != null) setState(() => _photoFile = File(picked.path));
  }

  Future<void> _register() async {
    if (!_formKey.currentState!.validate()) return;
    if (_photoFile == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Selecciona una foto de perfil')),
      );
      return;
    }
    setState(() => _loading = true);

    try {
      final fcmToken = await FcmService.initialize() ?? 'no-token';

      await AuthService.register(
        email:       _emailCtrl.text.trim(),
        password:    _passCtrl.text,
        fullName:    _nameCtrl.text.trim(),
        phoneNumber: _phoneCtrl.text.trim(),
        role:        _selectedRole ?? _roleCtrl.text.trim(),
        fcmToken:    fcmToken,
        photo:       _photoFile,
      );

      if (!mounted) return;
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => const UsersScreen()),
        (_) => false,
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
    return Scaffold(
      appBar: AppBar(title: const Text('Crear cuenta')),
      backgroundColor: const Color(0xFFF5F7FA),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(24),
          child: Form(
            key: _formKey,
            child: Column(
              children: [
                // ── Foto de perfil ─────────────────────────────────────────
                GestureDetector(
                  onTap: _pickPhoto,
                  child: Stack(
                    alignment: Alignment.bottomRight,
                    children: [
                      CircleAvatar(
                        radius: 54,
                        backgroundColor: Colors.grey.shade200,
                        backgroundImage:
                            _photoFile != null ? FileImage(_photoFile!) : null,
                        child: _photoFile == null
                            ? const Icon(Icons.person, size: 54, color: Colors.grey)
                            : null,
                      ),
                      Container(
                        decoration: BoxDecoration(
                          color: const Color(0xFF1565C0),
                          shape: BoxShape.circle,
                          border: Border.all(color: Colors.white, width: 2),
                        ),
                        padding: const EdgeInsets.all(6),
                        child: const Icon(Icons.camera_alt, color: Colors.white, size: 16),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 8),
                Text('Toca para seleccionar foto',
                    style: TextStyle(color: Colors.grey.shade600, fontSize: 12)),
                const SizedBox(height: 24),

                // ── Campos del formulario ──────────────────────────────────
                _buildTextField(
                  controller: _emailCtrl,
                  label: 'Email',
                  icon: Icons.email_outlined,
                  keyboardType: TextInputType.emailAddress,
                  validator: (v) =>
                      v == null || !v.contains('@') ? 'Email inválido' : null,
                ),
                const SizedBox(height: 14),
                _buildTextField(
                  controller: _passCtrl,
                  label: 'Contraseña',
                  icon: Icons.lock_outline,
                  obscureText: _obscurePass,
                  suffixIcon: IconButton(
                    icon: Icon(_obscurePass
                        ? Icons.visibility_outlined
                        : Icons.visibility_off_outlined),
                    onPressed: () => setState(() => _obscurePass = !_obscurePass),
                  ),
                  validator: (v) =>
                      v == null || v.length < 6 ? 'Mínimo 6 caracteres' : null,
                ),
                const SizedBox(height: 14),
                _buildTextField(
                  controller: _nameCtrl,
                  label: 'Nombre completo',
                  icon: Icons.badge_outlined,
                  validator: (v) =>
                      v == null || v.trim().isEmpty ? 'Campo obligatorio' : null,
                ),
                const SizedBox(height: 14),
                _buildTextField(
                  controller: _phoneCtrl,
                  label: 'Número telefónico',
                  icon: Icons.phone_outlined,
                  keyboardType: TextInputType.phone,
                  validator: (v) =>
                      v == null || v.trim().isEmpty ? 'Campo obligatorio' : null,
                ),
                const SizedBox(height: 14),

                // ── Selector de cargo ──────────────────────────────────────
                DropdownButtonFormField<String>(
                  value: _selectedRole,
                  decoration: InputDecoration(
                    labelText: 'Cargo',
                    prefixIcon: const Icon(Icons.work_outline),
                    border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                    contentPadding:
                        const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
                  ),
                  items: _roleOptions
                      .map((r) => DropdownMenuItem(value: r, child: Text(r)))
                      .toList(),
                  onChanged: (val) => setState(() => _selectedRole = val),
                  validator: (v) => v == null ? 'Selecciona un cargo' : null,
                ),
                const SizedBox(height: 28),

                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: _loading ? null : _register,
                    child: _loading
                        ? const SizedBox(
                            height: 20,
                            width: 20,
                            child:
                                CircularProgressIndicator(strokeWidth: 2, color: Colors.white),
                          )
                        : const Text('Crear cuenta', style: TextStyle(fontSize: 16)),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildTextField({
    required TextEditingController controller,
    required String label,
    required IconData icon,
    TextInputType? keyboardType,
    bool obscureText = false,
    Widget? suffixIcon,
    String? Function(String?)? validator,
  }) {
    return TextFormField(
      controller: controller,
      keyboardType: keyboardType,
      obscureText: obscureText,
      decoration: InputDecoration(
        labelText: label,
        prefixIcon: Icon(icon),
        suffixIcon: suffixIcon,
      ),
      validator: validator,
    );
  }

  @override
  void dispose() {
    _emailCtrl.dispose();
    _passCtrl.dispose();
    _nameCtrl.dispose();
    _phoneCtrl.dispose();
    _roleCtrl.dispose();
    super.dispose();
  }
}
