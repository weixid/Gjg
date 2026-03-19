try {
    final android.content.Context ctx = MainActivity.this;
    final float dp = ctx.getResources().getDisplayMetrics().density;
    final android.content.SharedPreferences vaultPrefs = ctx.getSharedPreferences("CyberVaultData", android.content.Context.MODE_PRIVATE);
    try {
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            android.app.AlarmManager am = (android.app.AlarmManager) ctx.getSystemService(android.content.Context.ALARM_SERVICE);
            if (am != null && !am.canScheduleExactAlarms()) {
                if (!vaultPrefs.getBoolean("pref_alarm_perm_granted", false)) {
                    vaultPrefs.edit().putBoolean("pref_alarm_perm_granted", true).apply();
                    android.widget.Toast.makeText(ctx, "⚠️ تکایە مۆڵەتی Alarms & Reminders پێ بدە", android.widget.Toast.LENGTH_LONG).show();
                    try {
                        android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        intent.setData(android.net.Uri.parse("package:" + ctx.getPackageName()));
                        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                    } catch(Exception e) {
                        try {
                            android.content.Intent intent2 = new android.content.Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            intent2.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                            ctx.startActivity(intent2);
                        } catch(Exception ex) {}
                    }
                }
            } else {
                vaultPrefs.edit().putBoolean("pref_alarm_perm_granted", true).apply();
            }
        }
    } catch (Exception e) {}
    final boolean isTablet = (ctx.getResources().getConfiguration().screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) >= android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
    final float tScale = isTablet ? 1.3f : 1f;
    final float sScale = isTablet ? 1.5f : 1f;
    MainActivity.this.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    if (vaultPrefs.getBoolean("pref_secure_screen", false)) {
        MainActivity.this.getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE);
    }
    if (!vaultPrefs.contains("pref_lang")) { vaultPrefs.edit().putString("pref_lang", "ku").apply(); }
    android.graphics.Typeface tempFont = android.graphics.Typeface.DEFAULT;
    try { tempFont = android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/kurd.ttf"); } catch (Exception e) {}
    final android.graphics.Typeface kurdFont = tempFont;
    final int[] wrongPasswordCount = {0};
    final java.util.ArrayList<String> intruderPhotoList = new java.util.ArrayList<>();
    class Utils {
        public void vibe(long ms) { try { android.os.Vibrator v = (android.os.Vibrator) ctx.getSystemService(android.content.Context.VIBRATOR_SERVICE); if(v != null && v.hasVibrator()) { v.vibrate(ms); } } catch(Exception e){} }
        public String formatTime(long millis) { try { java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd - HH:mm", java.util.Locale.ENGLISH); return sdf.format(new java.util.Date(millis)); } catch(Exception e) { return ""; } }
        public void hideKeyboard(android.view.View v) { try { android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) ctx.getSystemService(android.content.Context.INPUT_METHOD_SERVICE); if(imm != null && v != null) { imm.hideSoftInputFromWindow(v.getWindowToken(), 0); } } catch(Exception e){} }
        public void showKeyboard(android.view.View v) { try { android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) ctx.getSystemService(android.content.Context.INPUT_METHOD_SERVICE); if(imm != null && v != null) { imm.showSoftInput(v, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT); } } catch(Exception e){} }
    }
    final Utils tools = new Utils();
    class UI {
        public android.graphics.drawable.GradientDrawable glass(int fill, int stroke, float radius) { android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(); gd.setColor(fill); gd.setCornerRadius(radius * dp); gd.setStroke((int)(1.2f * dp), stroke); return gd; }
        public android.graphics.drawable.GradientDrawable premiumGlass(int fill, int stroke, float radius) { android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(); gd.setColor(fill); gd.setCornerRadius(radius * dp); gd.setStroke((int)(0.8f * dp), stroke); return gd; }
        public android.graphics.drawable.GradientDrawable gradientCard(int[] colors, float radius, android.graphics.drawable.GradientDrawable.Orientation orient) { android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(orient, colors); gd.setCornerRadius(radius * dp); return gd; }
        public android.graphics.drawable.GradientDrawable cardStyle(int fill, int stroke, float radius) {
            int style = vaultPrefs.getInt("pref_card_style", 0);
            android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
            gd.setCornerRadius(radius * dp);
            if(style == 0 || style == 1) { gd.setColor(fill); gd.setStroke((int)(1f * dp), stroke); }
            else if(style == 2) { gd.setColor(0x00000000); gd.setStroke((int)(0.8f * dp), stroke); }
            return gd;
        }
        public void bounce(final android.view.View v) { try { if(v != null) { v.animate().scaleX(0.92f).scaleY(0.92f).setDuration(80).setInterpolator(new android.view.animation.DecelerateInterpolator(2f)).withEndAction(new Runnable() { @Override public void run() { v.animate().scaleX(1f).scaleY(1f).setDuration(220).setInterpolator(new android.view.animation.OvershootInterpolator(2.5f)).start(); } }).start(); } } catch (Exception e) {} }
        public int getMainColor() { return vaultPrefs.getInt("pref_main_color", 0xFF00F0FF); }
        public int blendColor(int c1, int c2, float ratio) { float r2 = 1f - ratio; int a = (int)(((c1>>24)&0xFF)*ratio + ((c2>>24)&0xFF)*r2); int r = (int)(((c1>>16)&0xFF)*ratio + ((c2>>16)&0xFF)*r2); int g = (int)(((c1>>8)&0xFF)*ratio + ((c2>>8)&0xFF)*r2); int b = (int)((c1&0xFF)*ratio + (c2&0xFF)*r2); return (a<<24)|(r<<16)|(g<<8)|b; }
    }
    final UI ui = new UI();
    class StarryNightView extends android.view.View {
        private android.graphics.Paint paint;
        private android.graphics.Paint auroraPaint;
        private float[][] stars;
        private float[] shootingStar;
        private float auroraPhase = 0f;
        private java.util.Random rnd = new java.util.Random();
        public StarryNightView(android.content.Context c) {
            super(c);
            paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
            auroraPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
            stars = new float[180][5];
            for(int i=0; i<180; i++) {
                stars[i][0] = rnd.nextFloat() * 2000f;
                stars[i][1] = rnd.nextFloat() * 3000f;
                stars[i][2] = 1.2f + rnd.nextFloat() * 4.5f;
                stars[i][3] = rnd.nextFloat() * 255f;
                stars[i][4] = 1.5f + rnd.nextFloat() * 4f;
            }
            shootingStar = new float[]{0, 0, 0, 0, 0};
        }
        @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            for(int i=0; i<180; i++) { stars[i][0] = rnd.nextFloat() * w; stars[i][1] = rnd.nextFloat() * h; }
        }
        @Override protected void onDraw(android.graphics.Canvas canvas) {
            super.onDraw(canvas);
            int w = getWidth(); int h = getHeight();
            auroraPhase += 0.008f;
            if(w > 0 && h > 0) {
                float aY = h * 0.15f + (float)Math.sin(auroraPhase) * h * 0.08f;
                int aAlpha1 = (int)(18 + 12 * Math.sin(auroraPhase * 1.3f));
                int aAlpha2 = (int)(14 + 10 * Math.sin(auroraPhase * 0.7f + 1.2f));
                android.graphics.LinearGradient aGrad1 = new android.graphics.LinearGradient(0, aY - h*0.15f, 0, aY + h*0.25f, new int[]{0x00000000, (aAlpha1<<24)|0x00DDFF, (aAlpha2<<24)|0xBF5AF2, 0x00000000}, new float[]{0f, 0.3f, 0.65f, 1f}, android.graphics.Shader.TileMode.CLAMP);
                auroraPaint.setShader(aGrad1);
                canvas.drawRect(0, aY - h*0.15f, w, aY + h*0.25f, auroraPaint);
                auroraPaint.setShader(null);
            }
            for(int i=0; i<180; i++) {
                stars[i][3] += stars[i][4];
                if(stars[i][3] > 255f) { stars[i][3] = 255f; stars[i][4] *= -1; }
                if(stars[i][3] < 15f) { stars[i][3] = 15f; stars[i][4] *= -1; }
                int alpha = (int)stars[i][3];
                float r = stars[i][2];
                if(r > 3f) {
                    paint.setColor(0xFFFFFFFF); paint.setAlpha(Math.max(0, alpha / 4));
                    canvas.drawCircle(stars[i][0], stars[i][1], r * 2.5f, paint);
                }
                paint.setColor(0xFFFFFFFF); paint.setAlpha(alpha);
                canvas.drawCircle(stars[i][0], stars[i][1], r, paint);
                paint.setAlpha(Math.min(255, alpha + 80));
                canvas.drawCircle(stars[i][0], stars[i][1], r * 0.35f, paint);
            }
            if(shootingStar[0] == 0) {
                if(rnd.nextInt(120) < 2) {
                    shootingStar[0] = 1; shootingStar[1] = rnd.nextFloat() * getWidth();
                    shootingStar[2] = 0; shootingStar[3] = 20f + rnd.nextFloat() * 30f;
                    shootingStar[4] = 1f;
                }
            } else {
                shootingStar[1] -= shootingStar[3]; shootingStar[2] += shootingStar[3];
                shootingStar[4] -= 0.008f;
                int sAlpha = (int)(Math.max(0, shootingStar[4]) * 255);
                paint.setAlpha(sAlpha); paint.setStrokeWidth(3f);
                float sx = shootingStar[1]; float sy = shootingStar[2];
                paint.setColor(0xFFFFFFFF); paint.setAlpha(sAlpha);
                canvas.drawLine(sx, sy, sx+80f, sy-80f, paint);
                paint.setAlpha(sAlpha / 2);
                canvas.drawLine(sx+80f, sy-80f, sx+160f, sy-160f, paint);
                paint.setAlpha(sAlpha / 5);
                canvas.drawLine(sx+160f, sy-160f, sx+220f, sy-220f, paint);
                if(shootingStar[2] > getHeight() || shootingStar[1] < -200 || shootingStar[4] <= 0f) shootingStar[0] = 0;
            }
            if(getVisibility() == VISIBLE) invalidate();
        }
    }
    class ParadiseSkyView extends android.view.View {
        private android.graphics.Paint paint;
        private android.graphics.Paint snowPaint;
        private android.graphics.Paint glowPaint;
        private float[][] sp;
        private float[][] snow;
        private java.util.Random rnd = new java.util.Random();
        private int vw, vh;
        private float ambientPhase = 0f;
        private final String[] flakes = {"❄", "❅", "❆", "✦", "✧"};
        public ParadiseSkyView(android.content.Context c) {
            super(c);
            paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
            snowPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
            snowPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
            glowPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
            sp = new float[55][5];
            snow = new float[40][6];
        }
        @Override protected void onSizeChanged(int w, int h, int ow, int oh) {
            super.onSizeChanged(w,h,ow,oh); vw=w; vh=h;
            for(int i=0;i<55;i++) initSp(i,true);
            for(int i=0;i<40;i++) initSnow(i,true);
        }
        private void initSp(int i, boolean rand) {
            sp[i][0] = rnd.nextFloat() * vw;
            sp[i][1] = rand ? rnd.nextFloat() * vh : vh + 20f;
            sp[i][2] = 1.5f + rnd.nextFloat() * 5.5f;
            sp[i][3] = 0.3f + rnd.nextFloat() * 0.4f;
            sp[i][4] = 0.1f + rnd.nextFloat() * 0.35f;
        }
        private void initSnow(int i, boolean rand) {
            snow[i][0] = rnd.nextFloat() * vw;
            snow[i][1] = rand ? rnd.nextFloat() * vh : -50f;
            snow[i][2] = 18f + rnd.nextFloat() * 24f;
            snow[i][3] = 0.25f + rnd.nextFloat() * 0.45f;
            snow[i][4] = 0.2f + rnd.nextFloat() * 0.55f;
            snow[i][5] = (rnd.nextFloat() - 0.5f) * 1.0f;
        }
        @Override protected void onDraw(android.graphics.Canvas canvas) {
            if(vw==0||vh==0){if(getVisibility()==VISIBLE)invalidate();return;}
            ambientPhase += 0.012f;
            for(int i=0;i<55;i++) {
                sp[i][1] -= sp[i][4];
                sp[i][3] -= 0.0008f;
                if(sp[i][1] < -20f || sp[i][3] <= 0f) { initSp(i, false); continue; }
                float r = sp[i][2]; float a = sp[i][3];
                int ai = (int)(a*255);
                android.graphics.RadialGradient rg = new android.graphics.RadialGradient(sp[i][0],sp[i][1],r*3f, new int[]{(ai<<24)|0xFFF8E0,(Math.max(0,ai-60)<<24)|0xFFE0F0,0x00FFFFFF}, new float[]{0f,0.35f,1f}, android.graphics.Shader.TileMode.CLAMP);
                paint.setShader(rg); canvas.drawCircle(sp[i][0],sp[i][1],r*3f,paint);
                paint.setShader(null); paint.setColor((Math.min(255,ai+80)<<24)|0xFFFFFF);
                canvas.drawCircle(sp[i][0],sp[i][1],r*0.3f,paint);
            }
            for(int i=0;i<40;i++) {
                snow[i][1] += snow[i][4];
                snow[i][0] += snow[i][5] + (float)Math.sin(snow[i][1] * 0.015f + i * 0.3f) * 1.2f;
                if(snow[i][0] < -30f) snow[i][0] = vw + 10f;
                if(snow[i][0] > vw + 30f) snow[i][0] = -10f;
                if(snow[i][1] > vh + 40f) { initSnow(i, false); continue; }
                int sa = (int)(snow[i][3] * 255);
                snowPaint.setTextSize(snow[i][2]);
                snowPaint.setColor((sa << 24) | 0xFFFFFF);
                float rot = (float)Math.sin(ambientPhase + i * 0.5f) * 20f;
                canvas.save(); canvas.rotate(rot, snow[i][0], snow[i][1]);
                canvas.drawText(flakes[(i % 5)], snow[i][0], snow[i][1], snowPaint);
                canvas.restore();
            }
            if(getVisibility()==VISIBLE) invalidate();
        }
    }
    class Crypto {
        private static final String KEY = "SecretVaultKey2024!";
        public String compressAndEncrypt(String plainText) {
            try {
                if (plainText == null || plainText.isEmpty()) return plainText;
                java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                java.util.zip.Deflater deflater = new java.util.zip.Deflater(java.util.zip.Deflater.BEST_COMPRESSION);
                java.util.zip.DeflaterOutputStream dos = new java.util.zip.DeflaterOutputStream(baos, deflater);
                dos.write(plainText.getBytes("UTF-8")); dos.close();
                byte[] compressedBytes = baos.toByteArray();
                byte[] keyBytes = KEY.getBytes("UTF-8");
                byte[] result = new byte[compressedBytes.length];
                for (int i = 0; i < compressedBytes.length; i++) { result[i] = (byte) (compressedBytes[i] ^ keyBytes[i % keyBytes.length]); }
                return android.util.Base64.encodeToString(result, android.util.Base64.NO_WRAP);
            } catch (Exception e) { return null; }
        }
        public String decryptAndDecompress(String base64Text) {
            try {
                if (base64Text == null || base64Text.isEmpty()) return base64Text;
                byte[] encryptedBytes = android.util.Base64.decode(base64Text, android.util.Base64.NO_WRAP);
                byte[] keyBytes = KEY.getBytes("UTF-8");
                byte[] compressedBytes = new byte[encryptedBytes.length];
                for (int i = 0; i < encryptedBytes.length; i++) { compressedBytes[i] = (byte) (encryptedBytes[i] ^ keyBytes[i % keyBytes.length]); }
                try {
                    java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(compressedBytes);
                    java.util.zip.InflaterInputStream iis = new java.util.zip.InflaterInputStream(bais);
                    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                    byte[] buffer = new byte[1024]; int len;
                    while ((len = iis.read(buffer)) > 0) baos.write(buffer, 0, len);
                    iis.close(); baos.close(); return new String(baos.toByteArray(), "UTF-8");
                } catch(Exception e1) {
                    try {
                        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(compressedBytes);
                        java.util.zip.GZIPInputStream gis = new java.util.zip.GZIPInputStream(bais);
                        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                        byte[] buffer = new byte[1024]; int len;
                        while ((len = gis.read(buffer)) > 0) baos.write(buffer, 0, len);
                        gis.close(); baos.close(); return new String(baos.toByteArray(), "UTF-8");
                    } catch(Exception e2) { return null; }
                }
            } catch (Exception e) { return null; }
        }
        public String decryptLegacy(String encryptedText) {
            try {
                if (encryptedText == null || encryptedText.isEmpty()) return encryptedText;
                byte[] encryptedBytes = android.util.Base64.decode(encryptedText, android.util.Base64.NO_WRAP);
                byte[] keyBytes = KEY.getBytes("UTF-8");
                byte[] result = new byte[encryptedBytes.length];
                for (int i = 0; i < encryptedBytes.length; i++) { result[i] = (byte) (encryptedBytes[i] ^ keyBytes[i % keyBytes.length]); }
                return new String(result, "UTF-8");
            } catch (Exception e) { return encryptedText; }
        }
    }
    final Crypto crypto = new Crypto();
    class Trans {
        public String get(String key) {
            String l = vaultPrefs.getString("pref_lang", "ku");
            if(l.equals("en")) {
                switch(key) {
                    case "close": return "Close ❌"; case "vault_title": return "Secret Vault Pro"; case "search_hint": return "Search title or content... 🔍"; case "empty": return "No secrets found..."; case "hint_title": return "Note Title..."; case "hint_content": return "Write your secret here..."; case "save": return "Save Note 💾"; case "lock": return "Lock & Return 🔒"; case "set_title": return "Settings";
                    case "shake": return "Shake to Lock"; case "sub_shake": return "[ Device Sensor ]"; case "secure_screen": return "Prevent Screenshots"; case "sub_secure": return "[ FLAG_SECURE ]"; case "card_style": return "Card Style"; case "sub_style": return "[ Appearance ]"; case "main_color": return "Main Theme Color"; case "sub_color": return "[ Accent Color ]"; case "export_data": return "Export Data (JSON) 📤"; case "sub_exp": return "[ Backup ]"; case "import_data": return "Import Data 📥"; case "sub_imp": return "[ Restore ]"; case "delete_all": return "Delete All Data ⚠️"; case "auto_lock": return "Auto-Lock Timer"; case "sub_lock": return "[ Timeout ]";
                    case "active": return "Active 🟢"; case "inactive": return "Inactive 🔴"; case "back": return "Return"; case "cancel": return "Cancel"; case "confirm": return "Confirm"; case "copied": return "Copied 📋"; case "saved_ok": return "Saved ✅"; case "del_title": return "Delete Note 🗑️"; case "del_msg": return "Are you sure you want to delete this permanently?"; case "del_yes": return "Delete"; case "del_no": return "Cancel"; case "info_what": return "What is this section for?"; case "info_ok": return "Got it 👍"; case "tooltip_set": return "Tap the ℹ️ icon next to any setting for more details."; case "set_intro_title": return "Settings Guide 💡"; case "tut_redo": return "Replay Tutorial 🔁"; case "tut_next": return "Next ⏭️"; case "tut_done": return "Done ✅";
                    case "desc_shake": return "If someone approaches you, simply shake your phone quickly to instantly lock and hide the app."; case "desc_secure": return "Prevents anyone (even yourself) from taking screenshots or screen recordings while inside the app."; case "desc_style": return "Changes the visual appearance of the note cards: Glassmorphism or Minimalist outlines."; case "desc_color": return "Changes the primary accent color of the entire application to your favorite color."; case "desc_lock": return "Automatically locks the app and returns to the calculator screen if you are inactive for the selected time."; case "desc_exp": return "Compresses and encrypts all your secrets into a secure format, then copies it to your clipboard for backup."; case "desc_imp": return "Restores and decrypts your secrets from a previously copied backup text into the app.";
                    case "style_glass": return "Glassmorphism"; case "style_min": return "Minimalist";
                    case "al_30s": return "30 Sec"; case "al_1m": return "1 Min"; case "al_5m": return "5 Min"; case "al_never": return "Never";
                    case "tut_calc_t0": return "Step 1/2: Login"; case "tut_calc_d0": return "To enter the secret vault, long press the large ZERO at the top screen for 2 seconds, then enter your password."; case "tut_calc_t1": return "Step 2/2: Change Password"; case "tut_calc_d1": return "Whenever you want to change your password, long press the number 9 at the bottom for 2 seconds, then enter your old and new password.";
                    case "tut_vault_t0": return "Search Secrets 🔍"; case "tut_vault_d0": return "Type a title or content here to quickly find any of your saved secrets."; case "tut_vault_t1": return "Add New Secret ➕"; case "tut_vault_d1": return "Tap this floating button at the bottom to write and save a new secret note."; case "tut_vault_t2": return "Secret Cards 📝"; case "tut_vault_d2": return "Each note is a card. Tap ✏️ to edit, 📋 to copy, 🗑️ to delete, or ⭐ to favorite."; case "tut_vault_t3": return "Settings ⚙️"; case "tut_vault_d3": return "Tap the gear icon at the top to change colors, languages, and security settings."; case "tut_vault_t4": return "Death Timer ⏳"; case "tut_vault_d4": return "A security feature in Settings that automatically wipes your data if you don't open the app for a set time.";
                    case "cat_normal": return "Normal"; case "cat_important": return "Important"; case "cat_work": return "Work"; case "cat_personal": return "Personal";
                    case "pin_new": return "Set Password 🔐"; case "pin_enter": return "Enter Password 🔒"; case "pin_old": return "Old Password 🔑"; case "pin_new2": return "New Password 🆕"; case "pin_confirm_new": return "Confirm Password 🔄"; case "pin_no_match": return "Passwords do not match ❌"; case "pin_no_pass": return "Set a password first ⚠️"; case "pin_ok": return "Changed ✅"; case "pin_err": return "Wrong Password ❌";
                    case "scroll_hint": return "Scroll down for more ⬇️"; case "hint_9_toast": return "🔑 Long press 9 to change password"; case "hint_9_sub": return "[ Change Password Mode ]";
                    case "exp_ok": return "✅ Compressed Data Copied!"; case "imp_ok": return "✅ Data Restored!"; case "imp_err": return "❌ Invalid or corrupted code!";
                    case "dt_title": return "Death Timer"; case "dt_sub": return "[ Auto-Wipe ]"; case "dt_desc": return "If you don't open the app for the selected time, the app will automatically wipe all your data in the background.";
                    case "dt_time_label": return "Custom Time Limit:"; case "dt_action_wipe": return "🔴 Auto-Wipe Data"; case "dt_warning_note": return "⚠️ Note: Ensure the app is NOT battery restricted for the timer to work perfectly.";
                    case "vault_design_title": return "🎨 Vault Design"; case "vault_design_classic": return "Classic Design"; case "vault_design_modern": return "Modern Design"; case "vault_design_sub": return "[ Vault Theme ]";
                    case "vault_design_pro_title": return "🌟 Pro Designs"; case "vault_design_firefly": return "🌌 Firefly Night"; case "vault_design_paradise": return "🌤️ Sky Breeze";
                    case "intruder_log_btn": return "Intruder Selfie 📸"; case "intruder_log_title": return "Intruder Photos 🕵️"; case "intruder_empty": return "No intruder photos found."; case "intruder_toast": return "📸 Intruder photo captured!"; case "intruder_sub": return "[ Silent Camera ]"; case "pro_themes_sub": return "[ Pro Themes ]";
                    case "desc_intruder": return "If someone enters the wrong password 3 times in a row on the calculator screen, the app will silently activate the front camera and secretly take a photo of the person — without any sound, flash, or visible preview. The photo is saved with a timestamp inside the app's private storage. You can view all intruder photos by tapping this button.";
                    case "read_mode_close": return "Close ✕"; case "cam_perm_title": return "Camera Permission"; case "cam_perm_msg": return "To enable the Silent Selfie feature, the app needs Camera permission.\n\nDo you want to go to App Permissions and enable Camera?"; case "cam_perm_yes": return "Yes, Go ✅"; case "cam_perm_no": return "No"; case "auto_theme_title": return "🌗 Auto Day/Night"; case "auto_theme_sub": return "[ Auto Switch Theme ]"; case "auto_theme_desc": return "When enabled, the app automatically switches to Sky Breeze theme during the day (6AM-6PM) and Firefly Night theme at night. Only works with Pro designs."; case "auto_theme_on": return "Auto: ON"; case "auto_theme_off": return "Auto: OFF"; case "cam_grant_msg": return "📸  Camera permission granted!\n\nThe app is now fully ready. When someone enters the wrong password 3 times, the front camera silently captures their photo and saves it in the app.";
                }
            } else if (l.equals("ar")) {
                switch(key) {
                    case "close": return "إغلاق ❌"; case "vault_title": return "الخزنة الاحترافية"; case "search_hint": return "ابحث في العنوان أو المحتوى... 🔍"; case "empty": return "لا توجد أسرار..."; case "hint_title": return "عنوان الملاحظة..."; case "hint_content": return "اكتب سرك هنا..."; case "save": return "حفظ الملاحظة 💾"; case "lock": return "قفل وعودة 🔒"; case "set_title": return "الإعدادات";
                    case "shake": return "هز الهاتف للقفل"; case "sub_shake": return "[ اهتزاز الجهاز ]"; case "secure_screen": return "منع لقطة الشاشة"; case "sub_secure": return "[ شاشة آمنة ]"; case "card_style": return "شكل البطاقة"; case "sub_style": return "[ المظهر ]"; case "main_color": return "اللون الرئيسي"; case "sub_color": return "[ لون التطبيق ]"; case "export_data": return "تصدير البيانات 📤"; case "sub_exp": return "[ نسخ احتياطي ]"; case "import_data": return "استيراد البيانات 📥"; case "sub_imp": return "[ استعادة ]"; case "delete_all": return "حذف كل البيانات ⚠️"; case "auto_lock": return "القفل التلقائي"; case "sub_lock": return "[ وقت الانتظار ]";
                    case "active": return "م مفعل 🟢"; case "inactive": return "معطل 🔴"; case "back": return "عودة"; case "cancel": return "إلغاء"; case "confirm": return "تأكيد"; case "copied": return "تم النسخ 📋"; case "saved_ok": return "تم الحفظ ✅"; case "del_title": return "حذف الملاحظة 🗑️"; case "del_msg": return "هل أنت متأكد من الحذف نهائياً؟"; case "del_yes": return "حذف"; case "del_no": return "إلغاء"; case "info_what": return "ما فائدة هذا القسم؟"; case "info_ok": return "فهمت 👍"; case "tooltip_set": return "انقر على أيقونة ℹ️ بجانب أي إعداد لمعرفة المزيد."; case "set_intro_title": return "دليل الإعدادات 💡"; case "tut_redo": return "إعادة الشرح 🔁"; case "tut_next": return "التالي ⏭️"; case "tut_done": return "إنهاء ✅";
                    case "desc_shake": return "إذا اقترب منك أحد، قم بهز الهاتف بسرعة لقفل التطبيق وإخفائه فوراً."; case "desc_secure": return "يمنع التقاط لقطة شاشة أو تسجيل فيديو أثناء استخدام التطبيق."; case "desc_style": return "يغير المظهر المرئي للبطاقات: زجاجي أو خطوط بسيطة."; case "desc_color": return "يغير اللون الأساسي للتطبيق بالكامل إلى لونك المفضل."; case "desc_lock": return "يقفل التطبيق تلقائياً ويعود للحاسبة إذا لم تقم بأي نشاط للوقت المحدد."; case "desc_exp": return "يضغط ويشفر كل أسرارك إلى نص آمن وينسخه إلى الحافظة كنسخة احتياطية."; case "desc_imp": return "يستعيد ويفك تشفير أسرارك من نص منسوخ مسبقاً إلى التطبيق.";
                    case "style_glass": return "زجاجي"; case "style_min": return "بسيط";
                    case "al_30s": return "٣٠ ث"; case "al_1m": return "١ د"; case "al_5m": return "٥ د"; case "al_never": return "أبداً";
                    case "tut_calc_t0": return "الخطوة ١/٢: الدخول"; case "tut_calc_d0": return "للدخول إلى الخزنة السرية، اضغط مطولاً على الصفر الكبير في أعلى الشاشة لمدة ثانيتين، ثم أدخل كلمة المرور."; case "tut_calc_t1": return "الخطوة ٢/٢: تغيير الرمز"; case "tut_calc_d1": return "متى أردت تغيير كلمة المرور، اضغط مطولاً على الرقم ٩ بالأسفل لمدة ثانيتين، ثم أدخل الرمز القديم والجديد.";
                    case "tut_vault_t0": return "البحث في الأسرار 🔍"; case "tut_vault_d0": return "اكتب العنوان أو المحتوى هنا للعثور بسرعة على أي سر محفوظ."; case "tut_vault_t1": return "إضافة سر جديد ➕"; case "tut_vault_d1": return "انقر على هذا الزر العائم في الأسفل لكتابة وحفظ ملاحظة سرية جديدة."; case "tut_vault_t2": return "بطاقات الأسرار 📝"; case "tut_vault_d2": return "كل ملاحظة هي بطاقة. انقر ✏️ للتعديل، 📋 للنسخ، 🗑️ للحذف، أو ⭐ للمفضلة."; case "tut_vault_t3": return "الإعدادات ⚙️"; case "tut_vault_d3": return "انقر على أيقونة الترس بالأعلى لتغيير الألوان واللغة وإعدادات الأمان."; case "tut_vault_t4": return "مؤقت الموت ⏳"; case "tut_vault_d4": return "ميزة أمان في الإعدادات تقوم بمسح بياناتك تلقائياً إذا لم تفتح التطبيق لمدة محددة.";
                    case "cat_normal": return "عادي"; case "cat_important": return "مهم"; case "cat_work": return "عمل"; case "cat_personal": return "شخصي";
                    case "pin_new": return "تعيين رمز 🔐"; case "pin_enter": return "أدخل الرمز 🔒"; case "pin_old": return "الرمز القديم 🔑"; case "pin_new2": return "رمز جديد 🆕"; case "pin_confirm_new": return "تأكيد الرمز 🔄"; case "pin_no_match": return "الرموز غير متطابقة ❌"; case "pin_no_pass": return "عين رمزاً أولاً ⚠️"; case "pin_ok": return "تم التغيير ✅"; case "pin_err": return "الرمز خاطئ ❌";
                    case "scroll_hint": return "مرر للأسفل للمزيد ⬇️"; case "hint_9_toast": return "🔑 اضغط مطولاً على ٩ لتغيير الرمز"; case "hint_9_sub": return "[ Change Password Mode ]";
                    case "exp_ok": return "✅ تم نسخ البيانات المضغوطة!"; case "imp_ok": return "✅ تم استعادة البيانات!"; case "imp_err": return "❌ كود غير صالح أو تالف!";
                    case "dt_title": return "مؤقت الموت"; case "dt_sub": return "[ مسح تلقائي ]"; case "dt_desc": return "إذا لم تقم بفتح التطبيق للوقت المحدد، سيقوم التطبيق تلقائياً بمسح بياناتك في الخلفية لحمايتها.";
                    case "dt_time_label": return "الوقت المخصص:"; case "dt_action_wipe": return "🔴 مسح جميع البيانات تلقائياً"; case "dt_warning_note": return "⚠️ ملاحظة: ليعمل المؤقت بشكل مثالي، تأكد من عدم وضع التطبيق في وضع السكون (البطارية)";
                    case "vault_design_title": return "🎨 تصميم الخزنة"; case "vault_design_classic": return "تصميم كلاسيكي"; case "vault_design_modern": return "تصميم عصري"; case "vault_design_sub": return "[ مظهر الخزنة ]";
                    case "vault_design_pro_title": return "🌟 تصاميم برو"; case "vault_design_firefly": return "🌌 ليلة اليراعات"; case "vault_design_paradise": return "🌤️ نسيم السماء";
                    case "intruder_log_btn": return "صور المتطفلين 📸"; case "intruder_log_title": return "صور المتطفلين 🕵️"; case "intruder_empty": return "لا توجد صور للمتطفلين."; case "intruder_toast": return "📸 تم التقاط صورة المتطفل!"; case "intruder_sub": return "[ كاميرا صامتة ]"; case "pro_themes_sub": return "[ ثيمات برو ]";
                    case "desc_intruder": return "إذا أدخل شخص ما كلمة المرور الخاطئة 3 مرات متتالية على شاشة الآلة الحاسبة، سيقوم التطبيق تلقائياً بتفعيل الكاميرا الأمامية والتقاط صورة سرية للشخص — بدون أي صوت أو فلاش أو معاينة مرئية. يتم حفظ الصورة مع الوقت في تخزين خاص داخل التطبيق. يمكنك مراجعة جميع صور المتطفلين بالنقر على هذا الزر.";
                    case "read_mode_close": return "إغلاق ✕"; case "cam_perm_title": return "إذن الكاميرا"; case "cam_perm_msg": return "لتفعيل ميزة سيلفي التجسس، يحتاج التطبيق إلى إذن الكاميرا.\n\nهل تريد الذهاب إلى صلاحيات التطبيق لتفعيل الكاميرا؟"; case "cam_perm_yes": return "نعم، اذهب ✅"; case "cam_perm_no": return "لا"; case "auto_theme_title": return "🌗 تغيير تلقائي ليلي/نهاري"; case "auto_theme_sub": return "[ تبديل تلقائي ]"; case "auto_theme_desc": return "عند التفعيل، يتحول التطبيق تلقائياً إلى تصميم 'نسيم السماء' نهاراً وإلى 'ليلة اليراعات' ليلاً. يعمل فقط مع التصاميم الاحترافية."; case "auto_theme_on": return "تلقائي: شغّال"; case "auto_theme_off": return "تلقائي: مطفأ"; case "cam_grant_msg": return "📸  تم منح إذن الكاميرا!\n\nالتطبيق الآن جاهز بالكامل. عندما يدخل شخص ما كلمة المرور الخاطئة ٣ مرات، تلتقط الكاميرا الأمامية صورته بصمت وتحفظها في التطبيق.";
                }
            }
            switch(key) {
                case "close": return "داخستن ❌"; case "vault_title": return "گەنجینەی پڕۆ"; case "search_hint": return "گەڕان لە ناونیشان یان ناوەڕۆک... 🔍"; case "empty": return "هیچ تێبینییەک نەدۆزرایەوە..."; case "hint_title": return "ناونیشانی تێبینی..."; case "hint_content": return "ناوەڕۆکی نهێنییەکەت لێرە بنووسە..."; case "save": return "خەزنکردن 💾"; case "lock": return "داخستن و گەڕانەوە 🔒"; case "set_title": return "ڕێکخستنەکان";
                case "shake": return "هەژاندن بۆ داخستن"; case "sub_shake": return "[ Shake to Lock ]"; case "secure_screen": return "ڕێگریکردن لە سکرین شۆت"; case "sub_secure": return "[ Secure Screen / FLAG_SECURE ]"; case "card_style": return "شێوازی کارت"; case "sub_style": return "[ Card Appearance Style ]"; case "main_color": return "ڕەنگی سەرەکی ئەپ"; case "sub_color": return "[ Primary Accent Color ]"; case "export_data": return "دەرکردنی داتاکان (JSON) 📤"; case "sub_exp": return "[ Backup to JSON ]"; case "import_data": return "هێنانی داتاکان 📥"; case "sub_imp": return "[ Restore from JSON ]"; case "delete_all": return "سڕینەوەی هەموو داتاکان ⚠️"; case "auto_lock": return "خۆکار داخستن"; case "sub_lock": return "[ Auto-Lock Timeout ]";
                case "active": return "چالاکە 🟢"; case "inactive": return "کوژاوە 🔴"; case "back": return "گەڕانەوە"; case "cancel": return "پەشیمان بوونەوە"; case "confirm": return "پەسەندکردن"; case "copied": return "کۆپی کرا 📋"; case "saved_ok": return "خەزن کرا ✅"; case "del_title": return "سڕینەوەی تێبینی 🗑️"; case "del_msg": return "ئایا دڵنیایت دەتەوێت ئەم تێبینییە بە یەکجاری بسڕیتەوە؟"; case "del_yes": return "بەڵێ، بسڕەوە"; case "del_no": return "نەخێر"; case "info_what": return "ئەم بەشە بۆ چییە؟"; case "info_ok": return "تێگەیشتم 👍"; case "tooltip_set": return "بۆ زانیاری زیاتر پەنجە بنێ بە ئایکۆنی ℹ️ لە تەنیشت هەر بەشێکەوە."; case "set_intro_title": return "ڕێبەری ڕێکخستنەکان 💡"; case "tut_redo": return "نیشاندانی فێرکاری دووبارە 🔁"; case "tut_next": return "دواتر ⏭️"; case "tut_done": return "تەواو ✅";
                case "desc_shake": return "ئەگەر لە کاتی بەکارهێناندا کەسێک لێت نزیک بووەوە، تەنها مۆبایلەکەت بە خێرایی بهەژێنە بۆ ئەوەی ڕاستەوخۆ ئەپەکە دابخرێت و نهێنییەکانت پارێزراو بن."; case "desc_secure": return "ڕێگری دەکات لە خۆت یان هەر کەسێکی تر کە وێنەی شاشە (Screenshot) بگرێت یان شاشەکە تۆمار بکات (Screen Record) لە ناو ئەپەکەدا."; case "desc_style": return "شێوازی دەرکەوتنی کارتەکان دەگۆڕێت: شێوازی شوشەیی (Glass) یان شێوازی هێڵکاری سادە (Minimal)."; case "desc_color": return "ڕەنگی سەرەکی ئەپەکە و دوگمەکان دەگۆڕێت بۆ ئەو ڕەنگەی کە خۆت ئارەزووی دەکەیت."; case "desc_lock": return "ئەگەر بۆ ماوەیەک ئەپەکە بە کراوەیی جێبهێڵیت و بەکاری نەهێنیت، خۆکارانە قوفڵ دەبێت و دەگەڕێتەوە بۆ شاشەی حاسیبەکە بۆ پاراستنی داتاکانت."; case "desc_exp": return "هەموو تێبینییەکانت پێکەوە کۆمپرێس دەکات بۆ کەمکردنەوەی قەبارە و دواتر تەشفیری دەکات بۆ کۆدێکی زۆر بچووک و نەخوێنراوە، پاشان کۆپی دەکات بۆ ئەوەی بیپارێزیت."; case "desc_imp": return "ئەگەر پێشتر کۆدی داتاکانت کۆپی کردووە، لەم ڕێگەیەوە دەتوانیت کۆدەکە بکەیتەوە لە تەشفیر و هەموو تێبینییەکانت بهێنیتەوە ناو ئەپەکە.";
                case "style_glass": return "شوشەیی"; case "style_min": return "هێڵکاری سادە";
                case "al_30s": return "٣٠ چرکە"; case "al_1m": return "١ خولەک"; case "al_5m": return "٥ خولەک"; case "al_never": return "هەرگیز";
                case "tut_calc_t0": return "هەنگاوی ١/٢: چوونە ژوورەوە"; case "tut_calc_d0": return "بۆ چوونە ناو گەنجینە نهێنییەکە، پەنجە لەسەر شاشەی سەرەوە (سفرە گەورەکە) ڕابگرە بۆ ٢ چرکە دواتر پاسۆردەکەت بنووسە."; case "tut_calc_t1": return "هەنگاوی ٢/٢: گۆڕینی پاسۆرد"; case "tut_calc_d1": return "هەر کاتێک ویستت پاسۆردەکەت بگۆڕیت، پەنجە لەسەر ژمارە ٩ لە خوارەوە ڕابگرە بۆ ٢ چرکە دواتر پاسۆردی کۆن و نوێ بنووسە.";
                case "tut_vault_t0": return "گەڕان بەدوای نهێنییەکان 🔍"; case "tut_vault_d0": return "لەگەڵ زیادبوونی تێبینییەکانت، دەتوانیت ناونیشان یان ناوەڕۆکی هەر تێبینییەک لێرە بنووسیت بۆ ئەوەی بە خێرایی بیدۆزیتەوە."; case "tut_vault_t1": return "زیادکردنی تێبینی نوێ ➕"; case "tut_vault_d1": return "کلیک لەم دوگمە گەورەیەی خوارەوە بکە بۆ ئەوەی پەردەی نووسین بکرێتەوە و نهێنییەکی نوێ بە پۆلێنی جیاواز خەزن بکەیت."; case "tut_vault_t2": return "کارتی تێبینییەکان 📝"; case "tut_vault_d2": return "هەر تێبینییەک لە شێوەی کارتێکدایە. دەتوانیت کلیک لە ✏️ بکەیت بۆ دەستکاری، 📋 بۆ کۆپی، 🗑️ بۆ سڕینەوە، یان ⭐ بۆ ئەوەی بیکەیتە دڵخواز."; case "tut_vault_t3": return "ڕێکخستنەکان ⚙️"; case "tut_vault_d3": return "کلیک لە ئایکۆنی ڕێکخستن بکە لە سەرەوە بۆ گۆڕینی ڕەنگەکانی ئەپەکە، گۆڕینی زمان، یان زیادکردنی ئەمنییەت وەک ڕێگری لە سکرین شۆت."; case "tut_vault_t4": return "کاتژمێری مردن ⏳"; case "tut_vault_d4": return "تایبەتمەندییەکی ئەمنییە کە ئەگەر بۆ ماوەیەکی دیاریکراو نەچیتە ناو ئەپەکە، خۆکارانە داتاکانت دەسڕێتەوە.";
                case "cat_normal": return "ئاسایی"; case "cat_important": return "گرنگ"; case "cat_work": return "کار"; case "cat_personal": return "کەسی";
                case "pin_new": return "پاسۆرد دانێ 🔐"; case "pin_enter": return "پاسۆرد بنووسە 🔒"; case "pin_old": return "پاسۆردی کۆن 🔑"; case "pin_new2": return "پاسۆردی نوێ 🆕"; case "pin_confirm_new": return "دڵنیابوونەوەی پاسۆرد 🔄"; case "pin_no_match": return "پاسۆردەکان وەک یەک نین ❌"; case "pin_no_pass": return "سەرەتا پاسۆرد دابنێ ⚠️"; case "pin_ok": return "گۆڕدرا ✅"; case "pin_err": return "پاسۆرد هەڵەیە ❌";
                case "scroll_hint": return "زیاتر لە خوارەوە هەیە ⬇️"; case "hint_9_toast": return "🔑 پەنجە لەسەر ٩ ڕابگرە بۆ گۆڕینی پاسۆرد"; case "hint_9_sub": return "[ Long press 9 to change password ]";
                case "exp_ok": return "✅ داتای کۆمپرێسکراو کۆپی کرا!"; case "imp_ok": return "✅ داتاکان گەڕانەوە و کرانەوە!"; case "imp_err": return "❌ کۆدەکە هەڵەیە یان تێکچووە!";
                case "dt_title": return "کاتژمێری مردن"; case "dt_sub": return "[ سڕینەوەی خۆکارانە ]"; case "dt_desc": return "ئەگەر بۆ ئەو کاتەی دیاریت کردووە نەچوویتە ناو ئەپەکە، ئەپەکە لە پشتی پەردەوە بە بێدەنگی هەموو داتاکانت دەسڕێتەوە بۆ ئەوەی پارێزراو بن.";
                case "dt_time_label": return "کاتی دیاریکراوی خۆت:"; case "dt_action_wipe": return "🔴 سڕینەوەی خۆکارانەی داتا (Auto-Wipe)"; case "dt_warning_note": return "⚠️ تێبینی: تکایە دڵنیابە ئەپەکە لە بەشی 'باتری'دا لە 'خەو'دا نەبێت (Unrestricted).";
                case "vault_design_title": return "🎨 دیزاینی گەنجینە"; case "vault_design_classic": return "دیزاینی کلاسیک"; case "vault_design_modern": return "دیزاینی مۆدێرن"; case "vault_design_sub": return "[ Vault Theme ]";
                case "vault_design_pro_title": return "🌟 دیزاینەکانی پڕۆ"; case "vault_design_firefly": return "🌌 شەوەزەنگ"; case "vault_design_paradise": return "🌤️ ئاسمانی سەرسوور";
                case "intruder_log_btn": return "سێلفی دەستتێوەردان 📸"; case "intruder_log_title": return "وێنەی دەستتێوەردانەکان 🕵️"; case "intruder_empty": return "هیچ وێنەیەکی دەستتێوەردان نەدۆزرایەوە."; case "intruder_toast": return "📸 وێنەی داگیرکەر گیرا!"; case "intruder_sub": return "[ کامێرای نهێنی ]"; case "pro_themes_sub": return "[ دیزاینەکانی پڕۆ ]";
                case "desc_intruder": return "ئەم بەشە پارێزەری نهێنییەکانتە. ئەگەر کەسێک ٣ جار پاسۆرد بە هەڵە لێ بدات، ئەپەکە بە بێدەنگی وێنەی دەموچاوی دەگرێت و لێرە بۆت خەزن دەکات. وێنەکان تەنها تۆ دەتوانیت بیبینیت و لە گەلەری مۆبایلەکەدا دەرناکەون.";
                case "read_mode_close": return "داخستن ✕"; case "cam_perm_title": return "مۆڵەتی کامێرا"; case "cam_perm_msg": return "بۆ چالاک کردنی تایبەتمەندیی سێلفی نهێنی، پێویستت بە مۆڵەتی کامێراکەت هەیە.\n\nدەتەوێت بچیتە ناو مۆڵەتەکانی ئەپەکە و کامێرا چالاک بکەیت؟"; case "cam_perm_yes": return "بەڵێ، بڕۆ ✅"; case "cam_perm_no": return "نەخێر"; case "auto_theme_title": return "🌗 گۆڕینی خۆکاری شەو/ڕۆژ"; case "auto_theme_sub": return "[ خۆکاری ]"; case "auto_theme_desc": return "کاتێک چالاک بێت، ئەپەکە خۆکارانە کاتی ڕۆژ (٦ بەیانی تا ٦ ئێواران) دیزاینی ئاسمانی سەرسوور دەخاتەوە و کاتی شەو دیزاینی شەوەزەنگ. تەنها بۆ دیزاینەکانی پڕۆ کار دەکات."; case "auto_theme_on": return "خۆکاری: چالاکە"; case "auto_theme_off": return "خۆکاری: کوژاوە"; case "cam_grant_msg": return "📸  مۆڵەتی کامێرا دراوە!\n\nئەپەکەت ئێستا بە تەواوی ئامادەیە. کاتێک کەسێک ٣ جار پاسۆردی هەڵە بدات، کامێرای پێشەوەی مۆبایلەکەت بە بێدەنگی وێنەی دەموچاوی دەگرێت و لە ئەپەکەدا خەزن دەکرێت.";
            } return key;
        }
    }
    final Trans tr = new Trans();
    final Runnable executeDeathTimerAction = new Runnable() {
        @Override public void run() {
            try {
                vaultPrefs.edit().putString("secrets_array", "[]").putBoolean("pref_dt_enabled", false).apply();
                vaultPrefs.edit().putLong("last_open_time", System.currentTimeMillis()).apply();
            } catch (Exception e) {}
        }
    };
    if (MainActivity.this.getIntent().getBooleanExtra("is_death_timer_wakeup", false)) {
        executeDeathTimerAction.run();
        new android.os.Handler().postDelayed(new Runnable() { public void run() { MainActivity.this.finish(); } }, 500);
        return;
    }
    if (vaultPrefs.getBoolean("pref_dt_enabled", false)) {
        long lastOpen = vaultPrefs.getLong("last_open_time", System.currentTimeMillis());
        long limit = vaultPrefs.getLong("pref_dt_limit", 0);
        if (limit > 0 && (System.currentTimeMillis() - lastOpen) >= limit) {
            executeDeathTimerAction.run();
        }
    }
    final Runnable[] refreshDataListHolder = new Runnable[1];
    final int[] pinMode = {0};
    final String[] tempPin = new String[1];
    final long[] lastInteraction = {System.currentTimeMillis()};
    final boolean[] inVaultTut = {false};
    final android.widget.LinearLayout baseLinear = (android.widget.LinearLayout) findViewById(R.id.linear1); baseLinear.removeAllViews();
    final android.widget.FrameLayout masterCanvas = new android.widget.FrameLayout(ctx); masterCanvas.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -1)); masterCanvas.setBackgroundColor(0xFF000000); baseLinear.addView(masterCanvas);
    final android.widget.FrameLayout vaultLayer = new android.widget.FrameLayout(ctx); vaultLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); vaultLayer.setVisibility(android.view.View.GONE);
    final android.media.MediaPlayer[] proMediaPlayer = {null};
    final int[] proMediaDesign = {-1};
    final Runnable updateProAudio = new Runnable() {
        @Override public void run() {
            try {
                int curDesign = vaultPrefs.getInt("pref_vault_design", 0);
                boolean vaultVisible = (vaultLayer.getVisibility() == android.view.View.VISIBLE);
                if (vaultVisible && (curDesign == 2 || curDesign == 3)) {
                    int rawRes = (curDesign == 2) ? R.raw.shaw : R.raw.roz;
                    if (proMediaPlayer[0] != null && proMediaDesign[0] != curDesign) {
                        try { proMediaPlayer[0].stop(); proMediaPlayer[0].release(); } catch (Exception e2) {}
                        proMediaPlayer[0] = null; proMediaDesign[0] = -1;
                    }
                    if (proMediaPlayer[0] == null) {
                        proMediaPlayer[0] = android.media.MediaPlayer.create(ctx, rawRes);
                        if (proMediaPlayer[0] != null) { proMediaPlayer[0].setLooping(true); proMediaPlayer[0].start(); proMediaDesign[0] = curDesign; }
                    } else if (!proMediaPlayer[0].isPlaying()) { proMediaPlayer[0].start(); }
                } else {
                    if (proMediaPlayer[0] != null && proMediaPlayer[0].isPlaying()) { proMediaPlayer[0].pause(); }
                }
            } catch (Exception e) {}
        }
    };
    final StarryNightView starryNightView = new StarryNightView(ctx);
    starryNightView.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
    starryNightView.setVisibility(android.view.View.GONE);
    vaultLayer.addView(starryNightView);
    final ParadiseSkyView paradiseSkyView = new ParadiseSkyView(ctx);
    paradiseSkyView.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
    paradiseSkyView.setVisibility(android.view.View.GONE);
    vaultLayer.addView(paradiseSkyView);
    final android.view.View orb1 = new android.view.View(ctx); android.widget.FrameLayout.LayoutParams orb1Lp = new android.widget.FrameLayout.LayoutParams((int)(250*dp), (int)(250*dp)); orb1Lp.gravity = android.view.Gravity.CENTER; orb1Lp.setMargins(0, 0, (int)(100*dp), (int)(150*dp)); orb1.setLayoutParams(orb1Lp); vaultLayer.addView(orb1);
    final android.view.View orb2 = new android.view.View(ctx); android.widget.FrameLayout.LayoutParams orb2Lp = new android.widget.FrameLayout.LayoutParams((int)(200*dp), (int)(200*dp)); orb2Lp.gravity = android.view.Gravity.CENTER; orb2Lp.setMargins((int)(120*dp), (int)(150*dp), 0, 0); orb2.setLayoutParams(orb2Lp); vaultLayer.addView(orb2);
    android.animation.ObjectAnimator opY = android.animation.ObjectAnimator.ofFloat(orb1, "translationY", -60f, 60f); opY.setDuration(5000); opY.setRepeatCount(android.animation.ValueAnimator.INFINITE); opY.setRepeatMode(android.animation.ValueAnimator.REVERSE); opY.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator()); opY.start();
    android.animation.ObjectAnimator opX = android.animation.ObjectAnimator.ofFloat(orb1, "translationX", -25f, 25f); opX.setDuration(7200); opX.setRepeatCount(android.animation.ValueAnimator.INFINITE); opX.setRepeatMode(android.animation.ValueAnimator.REVERSE); opX.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator()); opX.start();
    android.animation.ObjectAnimator ocX = android.animation.ObjectAnimator.ofFloat(orb2, "translationX", -60f, 60f); ocX.setDuration(4200); ocX.setRepeatCount(android.animation.ValueAnimator.INFINITE); ocX.setRepeatMode(android.animation.ValueAnimator.REVERSE); ocX.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator()); ocX.start();
    android.animation.ObjectAnimator ocY = android.animation.ObjectAnimator.ofFloat(orb2, "translationY", 30f, -30f); ocY.setDuration(6000); ocY.setRepeatCount(android.animation.ValueAnimator.INFINITE); ocY.setRepeatMode(android.animation.ValueAnimator.REVERSE); ocY.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator()); ocY.start();
    final android.widget.LinearLayout vaultCardContainer = new android.widget.LinearLayout(ctx); vaultCardContainer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); vaultCardContainer.setOrientation(android.widget.LinearLayout.VERTICAL); vaultLayer.addView(vaultCardContainer);
    final android.widget.LinearLayout glassCardVault = new android.widget.LinearLayout(ctx); glassCardVault.setOrientation(android.widget.LinearLayout.VERTICAL); glassCardVault.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -1)); glassCardVault.setPadding((int)(15*dp*sScale), (int)(30*dp*sScale), (int)(15*dp*sScale), 0); vaultCardContainer.addView(glassCardVault);
    final android.widget.LinearLayout headerRow = new android.widget.LinearLayout(ctx); headerRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); android.widget.LinearLayout.LayoutParams hLp = new android.widget.LinearLayout.LayoutParams(-1, -2); hLp.setMargins(0, 0, 0, (int)(12*dp*sScale)); headerRow.setLayoutParams(hLp); headerRow.setGravity(android.view.Gravity.CENTER_VERTICAL); headerRow.setPadding((int)(18*dp), (int)(14*dp), (int)(14*dp), (int)(14*dp));
    final android.widget.TextView supportBtn = new android.widget.TextView(ctx); supportBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); supportBtn.setText("🎁"); supportBtn.setTextSize(22f * tScale); supportBtn.setPadding((int)(10*dp), (int)(5*dp), (int)(10*dp), (int)(5*dp)); headerRow.addView(supportBtn);
    final Runnable giftAnim = new Runnable() {
        @Override public void run() {
            try {
                if(supportBtn.getParent() != null && supportBtn.getVisibility() == android.view.View.VISIBLE) {
                    android.animation.ObjectAnimator rot = android.animation.ObjectAnimator.ofFloat(supportBtn, "rotation", 0f, 15f, -15f, 12f, -12f, 8f, -8f, 0f);
                    rot.setDuration(800); rot.setInterpolator(new android.view.animation.DecelerateInterpolator()); rot.start();
                    android.animation.ObjectAnimator sc = android.animation.ObjectAnimator.ofFloat(supportBtn, "scaleX", 1f, 1.15f, 1f);
                    sc.setDuration(500); sc.start();
                    android.animation.ObjectAnimator scy = android.animation.ObjectAnimator.ofFloat(supportBtn, "scaleY", 1f, 1.15f, 1f);
                    scy.setDuration(500); scy.start();
                }
                supportBtn.postDelayed(this, 4000);
            } catch(Exception e){}
        }
    };
    supportBtn.postDelayed(giftAnim, 2000);
    final android.widget.TextView vaultTitle = new android.widget.TextView(ctx); vaultTitle.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f)); vaultTitle.setTextSize(20f * tScale); vaultTitle.setTextColor(0xFFFFFFFF); vaultTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD);
    final android.widget.TextView settingsBtn = new android.widget.TextView(ctx); settingsBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); settingsBtn.setText("⚙️"); settingsBtn.setTextSize(22f * tScale); settingsBtn.setPadding((int)(10*dp), (int)(5*dp), (int)(10*dp), (int)(5*dp));
    headerRow.addView(vaultTitle); headerRow.addView(settingsBtn); glassCardVault.addView(headerRow);
    final android.widget.EditText searchInput = new android.widget.EditText(ctx); android.widget.LinearLayout.LayoutParams searchLp = new android.widget.LinearLayout.LayoutParams(-1, -2); searchLp.setMargins(0, 0, 0, (int)(10*dp*sScale)); searchInput.setLayoutParams(searchLp); searchInput.setTextColor(0xFFFFFFFF); searchInput.setTextSize(14f * tScale); searchInput.setTypeface(kurdFont); searchInput.setPadding((int)(15*dp), (int)(12*dp), (int)(15*dp), (int)(12*dp)); searchInput.setSingleLine(true); glassCardVault.addView(searchInput);
    final android.widget.ScrollView dataScroll = new android.widget.ScrollView(ctx); android.widget.LinearLayout.LayoutParams scrollLp = new android.widget.LinearLayout.LayoutParams(-1, 0, 1f); dataScroll.setLayoutParams(scrollLp); dataScroll.setVerticalScrollBarEnabled(false);
    try { if (android.os.Build.VERSION.SDK_INT >= 21) { dataScroll.setNestedScrollingEnabled(true); } dataScroll.setOverScrollMode(android.view.View.OVER_SCROLL_ALWAYS); } catch(Exception e){}
    final android.widget.LinearLayout dataListContainer = new android.widget.LinearLayout(ctx); dataListContainer.setOrientation(android.widget.LinearLayout.VERTICAL); dataListContainer.setPadding(0, 0, 0, (int)(80*dp*sScale)); dataScroll.addView(dataListContainer); glassCardVault.addView(dataScroll);
    dataScroll.setOnTouchListener(new android.view.View.OnTouchListener() {
        float startY = 0;
        @Override public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
            try {
                switch(event.getAction()) {
                    case android.view.MotionEvent.ACTION_DOWN: startY = event.getY(); break;
                    case android.view.MotionEvent.ACTION_MOVE: float deltaY = event.getY() - startY; if(!dataScroll.canScrollVertically(-1) && deltaY > 0) { dataListContainer.setTranslationY(deltaY * 0.3f); } else if(!dataScroll.canScrollVertically(1) && deltaY < 0) { dataListContainer.setTranslationY(deltaY * 0.3f); } else { dataListContainer.setTranslationY(0f); } break;
                    case android.view.MotionEvent.ACTION_UP: case android.view.MotionEvent.ACTION_CANCEL: dataListContainer.animate().translationY(0f).setDuration(500).setInterpolator(new android.view.animation.OvershootInterpolator(2f)).start(); break;
                }
            } catch(Exception e){} return false;
        }
    });
    dataScroll.getViewTreeObserver().addOnScrollChangedListener(new android.view.ViewTreeObserver.OnScrollChangedListener() {
        @Override public void onScrollChanged() {
            try {
                final int sY = dataScroll.getScrollY();
                final int sH = dataScroll.getHeight();
                for(int ci=0; ci<dataListContainer.getChildCount(); ci++) {
                    android.view.View card = dataListContainer.getChildAt(ci);
                    if(card==null) continue;
                    int top = card.getTop() - sY;
                    int bot = card.getBottom() - sY;
                    float vis;
                    if(top >= sH || bot <= 0) vis = 0.08f;
                    else if(top < 0) vis = Math.min(1f,(float)bot/(float)Math.max(1,card.getHeight()));
                    else vis = Math.min(1f,(float)(sH-top)/(float)Math.max(1,Math.min(sH,card.getHeight()+60)));
                    card.setAlpha(0.1f + vis*0.9f);
                }
            } catch(Exception e){}
        }
    });
    final android.widget.TextView fabAdd = new android.widget.TextView(ctx); android.widget.FrameLayout.LayoutParams fabLp = new android.widget.FrameLayout.LayoutParams((int)(65*dp), (int)(65*dp)); fabLp.gravity = android.view.Gravity.BOTTOM | android.view.Gravity.END; fabLp.setMargins(0, 0, (int)(20*dp*sScale), (int)(20*dp*sScale)); fabAdd.setLayoutParams(fabLp); fabAdd.setText("+"); fabAdd.setTextSize(32f * tScale); fabAdd.setTypeface(null, android.graphics.Typeface.BOLD); fabAdd.setGravity(android.view.Gravity.CENTER);
    try { if (android.os.Build.VERSION.SDK_INT >= 21) fabAdd.setElevation(20f); } catch(Exception e){} vaultLayer.addView(fabAdd);
    final android.view.View butterflyView = new android.view.View(ctx) {
        private float wingAngle = 0f;
        private float wingDir = 1f;
        private float floatPhase = 0f;
        private android.graphics.Paint wp = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        private android.graphics.Paint glowP = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        @Override protected void onDraw(android.graphics.Canvas canvas) {
            wingAngle += wingDir * 6f;
            if(wingAngle > 45f || wingAngle < 0f) wingDir *= -1f;
            floatPhase += 0.04f;
            float scaleX = (float)Math.cos(Math.toRadians(wingAngle));
            float cx = getWidth() / 2f; float cy = getHeight() / 2f + (float)Math.sin(floatPhase) * 5f;
            float r1x = 40f; float r1y = 30f; float r2x = 28f; float r2y = 20f;
            glowP.setColor(0x15FFB6DE);
            canvas.drawCircle(cx, cy, 55f, glowP);
            wp.setStyle(android.graphics.Paint.Style.FILL);
            wp.setColor(0xDDFFB6DE); canvas.save(); canvas.scale(-scaleX, 1f, cx, cy); canvas.drawOval(cx - r1x - 6f, cy - r1y, cx - 4f, cy + r1y, wp); canvas.restore();
            wp.setColor(0xDDC8A0F5); canvas.save(); canvas.scale(-scaleX, 1f, cx, cy); canvas.drawOval(cx - r2x - 4f, cy, cx - 2f, cy + r2y + 6f, wp); canvas.restore();
            wp.setColor(0xDDFFB6DE); canvas.save(); canvas.scale(scaleX, 1f, cx, cy); canvas.drawOval(cx + 4f, cy - r1y, cx + r1x + 6f, cy + r1y, wp); canvas.restore();
            wp.setColor(0xDDC8A0F5); canvas.save(); canvas.scale(scaleX, 1f, cx, cy); canvas.drawOval(cx + 2f, cy, cx + r2x + 4f, cy + r2y + 6f, wp); canvas.restore();
            wp.setColor(0xFF7A3888); wp.setStrokeWidth(2.5f); wp.setStyle(android.graphics.Paint.Style.FILL);
            canvas.drawOval(cx - 2.5f, cy - 20f, cx + 2.5f, cy + 20f, wp);
            wp.setColor(0xFF7A3888); wp.setStrokeWidth(1.2f); wp.setStyle(android.graphics.Paint.Style.STROKE);
            canvas.drawLine(cx - 1f, cy - 20f, cx - 12f, cy - 34f, wp);
            canvas.drawLine(cx + 1f, cy - 20f, cx + 12f, cy - 34f, wp);
            wp.setStyle(android.graphics.Paint.Style.FILL);
            canvas.drawCircle(cx - 12f, cy - 35f, 2.2f, wp); canvas.drawCircle(cx + 12f, cy - 35f, 2.2f, wp);
            if(getVisibility() == VISIBLE) invalidate();
        }
    };
    android.widget.FrameLayout.LayoutParams bfLp = new android.widget.FrameLayout.LayoutParams((int)(90*dp), (int)(90*dp));
    bfLp.gravity = android.view.Gravity.BOTTOM | android.view.Gravity.END;
    bfLp.setMargins(0, 0, (int)(95*dp*sScale), (int)(18*dp*sScale));
    butterflyView.setLayoutParams(bfLp);
    butterflyView.setVisibility(android.view.View.GONE);
    butterflyView.setClickable(false);
    vaultLayer.addView(butterflyView);
    final android.widget.FrameLayout editLayer = new android.widget.FrameLayout(ctx); editLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); editLayer.setBackgroundColor(0xEE040410); editLayer.setVisibility(android.view.View.GONE); editLayer.setClickable(true);
    final android.widget.LinearLayout editCard = new android.widget.LinearLayout(ctx); editCard.setOrientation(android.widget.LinearLayout.VERTICAL); android.widget.FrameLayout.LayoutParams eCardLp = new android.widget.FrameLayout.LayoutParams(-1, -2); eCardLp.gravity = android.view.Gravity.CENTER; eCardLp.setMargins((int)(20*dp*sScale), 0, (int)(20*dp*sScale), 0); editCard.setLayoutParams(eCardLp); editCard.setPadding((int)(20*dp), (int)(20*dp), (int)(20*dp), (int)(20*dp));
    final android.widget.EditText inputTitle = new android.widget.EditText(ctx); inputTitle.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); inputTitle.setTextColor(0xFFFFFFFF); inputTitle.setTextSize(18f * tScale); inputTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); inputTitle.setPadding((int)(15*dp), (int)(15*dp), (int)(15*dp), (int)(15*dp)); inputTitle.setSingleLine(true); inputTitle.setHint(tr.get("hint_title")); inputTitle.setHintTextColor(0x77FFFFFF); editCard.addView(inputTitle);
    android.view.View editSpace1 = new android.view.View(ctx); editSpace1.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, (int)(10*dp))); editCard.addView(editSpace1);
    final android.widget.EditText inputContent = new android.widget.EditText(ctx); inputContent.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); inputContent.setMinHeight((int)(120*dp*sScale)); inputContent.setMaxHeight((int)(250*dp*sScale)); inputContent.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE); inputContent.setGravity(android.view.Gravity.TOP | android.view.Gravity.START); inputContent.setTextColor(0xFFDDDDDD); inputContent.setTextSize(16f * tScale); inputContent.setTypeface(kurdFont); inputContent.setPadding((int)(15*dp), (int)(15*dp), (int)(15*dp), (int)(15*dp)); inputContent.setHint(tr.get("hint_content")); inputContent.setHintTextColor(0x77FFFFFF); editCard.addView(inputContent);
    android.view.View editSpace2 = new android.view.View(ctx); editSpace2.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, (int)(15*dp))); editCard.addView(editSpace2);
    final android.widget.LinearLayout catRow = new android.widget.LinearLayout(ctx); catRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); catRow.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2));
    final android.widget.TextView[] catBtns = new android.widget.TextView[4]; final int[] catColors = {0xFF00F0FF, 0xFFFF4444, 0xFFFF9F0A, 0xFFBF5AF2}; final int[] selectedCat = {0}; final String[] editingId = {""};
    for(int c=0; c<4; c++) {
        catBtns[c] = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams cLp = new android.widget.LinearLayout.LayoutParams(0, -2, 1f); cLp.setMargins((int)(2*dp), 0, (int)(2*dp), 0); catBtns[c].setLayoutParams(cLp); catBtns[c].setTextSize(12f * tScale); catBtns[c].setTypeface(kurdFont); catBtns[c].setGravity(android.view.Gravity.CENTER); catBtns[c].setPadding(0, (int)(10*dp), 0, (int)(10*dp));
        final int finalC = c; catBtns[c].setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); selectedCat[0] = finalC; for(int i=0; i<4; i++) { if(i==finalC) { catBtns[i].setBackground(ui.glass(0x44000000 | (catColors[i] & 0xFFFFFF), catColors[i], 10f)); catBtns[i].setTextColor(catColors[i]); } else { catBtns[i].setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 10f)); catBtns[i].setTextColor(0xFF888888); } } } }); catRow.addView(catBtns[c]);
    }
    editCard.addView(catRow); android.view.View editSpace3 = new android.view.View(ctx); editSpace3.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, (int)(20*dp))); editCard.addView(editSpace3);
    final android.widget.LinearLayout actionRow = new android.widget.LinearLayout(ctx); actionRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); actionRow.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2));
    final android.widget.TextView cancelEditBtn = new android.widget.TextView(ctx); cancelEditBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f)); cancelEditBtn.setTextSize(14f * tScale); cancelEditBtn.setTextColor(0xFFFFFFFF); cancelEditBtn.setTypeface(kurdFont); cancelEditBtn.setGravity(android.view.Gravity.CENTER); cancelEditBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); actionRow.addView(cancelEditBtn);
    android.view.View editSpace4 = new android.view.View(ctx); editSpace4.setLayoutParams(new android.widget.LinearLayout.LayoutParams((int)(10*dp), -1)); actionRow.addView(editSpace4);
    final android.widget.TextView saveEditBtn = new android.widget.TextView(ctx); saveEditBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f)); saveEditBtn.setTextSize(14f * tScale); saveEditBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); saveEditBtn.setGravity(android.view.Gravity.CENTER); saveEditBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); actionRow.addView(saveEditBtn);
    editCard.addView(actionRow); editLayer.addView(editCard);
    final android.widget.FrameLayout infoLayer = new android.widget.FrameLayout(ctx); infoLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); infoLayer.setBackgroundColor(0xCC000000); infoLayer.setVisibility(android.view.View.GONE); infoLayer.setClickable(true);
    final android.widget.LinearLayout infoCard = new android.widget.LinearLayout(ctx); infoCard.setOrientation(android.widget.LinearLayout.VERTICAL);
    android.widget.FrameLayout.LayoutParams iCardLp = new android.widget.FrameLayout.LayoutParams(-1, -2);
    iCardLp.gravity = android.view.Gravity.CENTER;
    iCardLp.setMargins((int)(30*dp*sScale), (int)(60*dp), (int)(30*dp*sScale), (int)(60*dp));
    infoCard.setLayoutParams(iCardLp);
    infoCard.setPadding((int)(25*dp), (int)(30*dp), (int)(25*dp), (int)(25*dp));
    final android.widget.TextView infoIcon = new android.widget.TextView(ctx); infoIcon.setTextSize(42f * tScale); infoIcon.setGravity(android.view.Gravity.CENTER); infoIcon.setPadding(0, 0, 0, (int)(10*dp)); infoCard.addView(infoIcon);
    final android.widget.TextView infoTitle = new android.widget.TextView(ctx); infoTitle.setTextSize(18f * tScale); infoTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); infoTitle.setGravity(android.view.Gravity.CENTER); infoTitle.setPadding(0, 0, 0, (int)(12*dp)); infoCard.addView(infoTitle);
    final android.widget.TextView infoDesc = new android.widget.TextView(ctx); infoDesc.setTextSize(14f * tScale); infoDesc.setTextColor(0xFFDDDDDD); infoDesc.setTypeface(kurdFont); infoDesc.setGravity(android.view.Gravity.CENTER); infoDesc.setLineSpacing(5f, 1.3f); infoDesc.setPadding(0, 0, 0, (int)(8*dp)); infoCard.addView(infoDesc);
    final android.widget.TextView infoEnTxt = new android.widget.TextView(ctx); infoEnTxt.setTextSize(11f * tScale); infoEnTxt.setTextColor(0xFF888888); infoEnTxt.setTypeface(kurdFont); infoEnTxt.setGravity(android.view.Gravity.CENTER); infoEnTxt.setPadding(0, 0, 0, (int)(22*dp)); infoCard.addView(infoEnTxt);
    final android.widget.TextView infoOkBtn = new android.widget.TextView(ctx); infoOkBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); infoOkBtn.setTextSize(15f * tScale); infoOkBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); infoOkBtn.setGravity(android.view.Gravity.CENTER); infoOkBtn.setPadding(0, (int)(14*dp), 0, (int)(14*dp)); infoCard.addView(infoOkBtn); infoLayer.addView(infoCard);
    final String[] currentInfo = new String[2];
    final String[] currentInfoIcon = {""};
    final Runnable showInfoDialog = new Runnable() { public void run() {
        String iconMap = "";
        switch(currentInfo[0]) {
            case "desc_shake": iconMap = "📳"; break;
            case "desc_secure": iconMap = "🔒"; break;
            case "desc_style": iconMap = "🎨"; break;
            case "desc_color": iconMap = "🖌️"; break;
            case "desc_lock": iconMap = "⏱️"; break;
            case "desc_exp": iconMap = "📤"; break;
            case "desc_imp": iconMap = "📥"; break;
            case "dt_desc": iconMap = "☠️"; break;
            default: iconMap = "ℹ️"; break;
        }
        infoIcon.setText(iconMap);
        infoTitle.setText(tr.get("info_what")); infoDesc.setText(tr.get(currentInfo[0])); infoEnTxt.setText(tr.get(currentInfo[1])); infoOkBtn.setText(tr.get("info_ok"));
        infoLayer.setVisibility(android.view.View.VISIBLE); infoLayer.setAlpha(0f); infoCard.setScaleX(0.88f); infoCard.setScaleY(0.88f); infoCard.setTranslationY(30f);
        try{ android.animation.ObjectAnimator.ofFloat(infoLayer, "alpha", 0f, 1f).setDuration(320).start(); android.animation.ObjectAnimator sx = android.animation.ObjectAnimator.ofFloat(infoCard, "scaleX", 0.88f, 1f); sx.setDuration(380); sx.setInterpolator(new android.view.animation.OvershootInterpolator(1.2f)); sx.start(); android.animation.ObjectAnimator sy = android.animation.ObjectAnimator.ofFloat(infoCard, "scaleY", 0.88f, 1f); sy.setDuration(380); sy.setInterpolator(new android.view.animation.OvershootInterpolator(1.2f)); sy.start(); android.animation.ObjectAnimator.ofFloat(infoCard, "translationY", 30f, 0f).setDuration(350).start(); }catch(Exception e){}
    } };
    infoOkBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); try{ android.animation.ObjectAnimator fade = android.animation.ObjectAnimator.ofFloat(infoLayer, "alpha", 1f, 0f); fade.setDuration(200); fade.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { infoLayer.setVisibility(android.view.View.GONE); }}); fade.start(); }catch(Exception e){} } });
    final android.widget.FrameLayout setIntroLayer = new android.widget.FrameLayout(ctx); setIntroLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); setIntroLayer.setBackgroundColor(0xDD000000); setIntroLayer.setVisibility(android.view.View.GONE); setIntroLayer.setClickable(true);
    final android.widget.LinearLayout setIntroCard = new android.widget.LinearLayout(ctx); setIntroCard.setOrientation(android.widget.LinearLayout.VERTICAL); android.widget.FrameLayout.LayoutParams siCardLp = new android.widget.FrameLayout.LayoutParams(-1, -2); siCardLp.gravity = android.view.Gravity.CENTER; siCardLp.setMargins((int)(30*dp*sScale), 0, (int)(30*dp*sScale), 0); setIntroCard.setLayoutParams(siCardLp); setIntroCard.setPadding((int)(25*dp), (int)(30*dp), (int)(25*dp), (int)(30*dp));
    final android.widget.TextView setIntroTitle = new android.widget.TextView(ctx); setIntroTitle.setTextSize(20f * tScale); setIntroTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); setIntroTitle.setGravity(android.view.Gravity.CENTER); setIntroTitle.setPadding(0, 0, 0, (int)(15*dp)); setIntroCard.addView(setIntroTitle);
    final android.widget.TextView setIntroMsg = new android.widget.TextView(ctx); setIntroMsg.setTextSize(15f * tScale); setIntroMsg.setTextColor(0xFFFFFFFF); setIntroMsg.setTypeface(kurdFont); setIntroMsg.setGravity(android.view.Gravity.CENTER); setIntroMsg.setLineSpacing(5f, 1.2f); setIntroMsg.setPadding(0, 0, 0, (int)(25*dp)); setIntroCard.addView(setIntroMsg);
    final android.widget.TextView setIntroBtn = new android.widget.TextView(ctx); setIntroBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); setIntroBtn.setTextSize(14f * tScale); setIntroBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); setIntroBtn.setGravity(android.view.Gravity.CENTER); setIntroBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); setIntroCard.addView(setIntroBtn);
    setIntroLayer.addView(setIntroCard);
    setIntroBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); try{ android.animation.ObjectAnimator fade = android.animation.ObjectAnimator.ofFloat(setIntroLayer, "alpha", 1f, 0f); fade.setDuration(200); fade.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { setIntroLayer.setVisibility(android.view.View.GONE); }}); fade.start(); }catch(Exception e){} } });
    final android.widget.FrameLayout tutLayer = new android.widget.FrameLayout(ctx); tutLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); tutLayer.setBackgroundColor(0xDD000000); tutLayer.setVisibility(android.view.View.GONE); tutLayer.setClickable(true);
    final android.widget.LinearLayout tutCard = new android.widget.LinearLayout(ctx); tutCard.setOrientation(android.widget.LinearLayout.VERTICAL); tutCard.setLayoutParams(iCardLp); tutCard.setPadding((int)(20*dp), (int)(30*dp), (int)(20*dp), (int)(30*dp));
    final android.widget.TextView tutIcon = new android.widget.TextView(ctx); tutIcon.setTextSize(50f * tScale); tutIcon.setGravity(android.view.Gravity.CENTER); tutIcon.setPadding(0, 0, 0, (int)(10*dp)); tutCard.addView(tutIcon);
    final android.widget.TextView tutTitle = new android.widget.TextView(ctx); tutTitle.setTextSize(20f * tScale); tutTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); tutTitle.setGravity(android.view.Gravity.CENTER); tutTitle.setPadding(0, 0, 0, (int)(15*dp)); tutCard.addView(tutTitle);
    final android.widget.TextView tutDesc = new android.widget.TextView(ctx); tutDesc.setTextSize(15f * tScale); tutDesc.setTextColor(0xFFFFFFFF); tutDesc.setTypeface(kurdFont); tutDesc.setGravity(android.view.Gravity.CENTER); tutDesc.setLineSpacing(5f, 1.2f); tutDesc.setPadding(0, 0, 0, (int)(25*dp)); tutCard.addView(tutDesc);
    final android.widget.TextView tutNextBtn = new android.widget.TextView(ctx); tutNextBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); tutNextBtn.setTextSize(16f * tScale); tutNextBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); tutNextBtn.setGravity(android.view.Gravity.CENTER); tutNextBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); tutCard.addView(tutNextBtn); tutLayer.addView(tutCard);
    final int[] tutStep = {0};
    final Runnable updateTutUI = new Runnable() { public void run() {
        if(!inVaultTut[0]) {
            if(tutStep[0] == 0) { tutIcon.setText("🔐"); tutTitle.setText(tr.get("tut_calc_t0")); tutDesc.setText(tr.get("tut_calc_d0")); tutNextBtn.setText(tr.get("tut_next")); }
            else if(tutStep[0] == 1) { tutIcon.setText("🔑"); tutTitle.setText(tr.get("tut_calc_t1")); tutDesc.setText(tr.get("tut_calc_d1")); tutNextBtn.setText(tr.get("tut_done")); }
            else { vaultPrefs.edit().putBoolean("tut_calc_seen", true).apply(); try{ android.animation.ObjectAnimator fade = android.animation.ObjectAnimator.ofFloat(tutLayer, "alpha", 1f, 0f); fade.setDuration(250); fade.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { tutLayer.setVisibility(android.view.View.GONE); }}); fade.start(); }catch(Exception e){} return; }
        } else {
            if(tutStep[0] == 0) { tutIcon.setText("🔍"); tutTitle.setText(tr.get("tut_vault_t0")); tutDesc.setText(tr.get("tut_vault_d0")); tutNextBtn.setText(tr.get("tut_next")); }
            else if(tutStep[0] == 1) { tutIcon.setText("➕"); tutTitle.setText(tr.get("tut_vault_t1")); tutDesc.setText(tr.get("tut_vault_d1")); tutNextBtn.setText(tr.get("tut_next")); }
            else if(tutStep[0] == 2) { tutIcon.setText("📝"); tutTitle.setText(tr.get("tut_vault_t2")); tutDesc.setText(tr.get("tut_vault_d2")); tutNextBtn.setText(tr.get("tut_next")); }
            else if(tutStep[0] == 3) { tutIcon.setText("⚙️"); tutTitle.setText(tr.get("tut_vault_t3")); tutDesc.setText(tr.get("tut_vault_d3")); tutNextBtn.setText(tr.get("tut_next")); }
            else if(tutStep[0] == 4) { tutIcon.setText("⏳"); tutTitle.setText(tr.get("tut_vault_t4")); tutDesc.setText(tr.get("tut_vault_d4")); tutNextBtn.setText(tr.get("tut_done")); }
            else { vaultPrefs.edit().putBoolean("tut_vault_seen", true).apply(); try{ android.animation.ObjectAnimator fade = android.animation.ObjectAnimator.ofFloat(tutLayer, "alpha", 1f, 0f); fade.setDuration(250); fade.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { tutLayer.setVisibility(android.view.View.GONE); }}); fade.start(); }catch(Exception e){} return; }
        }
        try{ tutCard.setScaleX(0.8f); tutCard.setScaleY(0.8f); tutCard.setAlpha(0f); android.animation.ObjectAnimator.ofFloat(tutCard, "scaleX", 0.8f, 1f).setDuration(300).start(); android.animation.ObjectAnimator.ofFloat(tutCard, "scaleY", 0.8f, 1f).setDuration(300).start(); android.animation.ObjectAnimator.ofFloat(tutCard, "alpha", 0f, 1f).setDuration(300).start(); }catch(Exception e){}
    }};
    tutNextBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); try{ android.animation.ObjectAnimator slide = android.animation.ObjectAnimator.ofFloat(tutCard, "translationX", 0f, -500f); android.animation.ObjectAnimator alpha = android.animation.ObjectAnimator.ofFloat(tutCard, "alpha", 1f, 0f); slide.setDuration(200); alpha.setDuration(200); slide.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { tutStep[0]++; tutCard.setTranslationX(0f); updateTutUI.run(); }}); slide.start(); alpha.start(); }catch(Exception e){} }});
    final android.widget.FrameLayout settingsLayer = new android.widget.FrameLayout(ctx); settingsLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); settingsLayer.setBackgroundColor(0xDD000000); settingsLayer.setVisibility(android.view.View.GONE); settingsLayer.setClickable(true);
    final android.widget.LinearLayout setCard = new android.widget.LinearLayout(ctx); setCard.setOrientation(android.widget.LinearLayout.VERTICAL); android.widget.FrameLayout.LayoutParams sCardLp = new android.widget.FrameLayout.LayoutParams(-1, -1); sCardLp.setMargins((int)(15*dp*sScale), (int)(40*dp*sScale), (int)(15*dp*sScale), (int)(40*dp*sScale)); setCard.setLayoutParams(sCardLp); setCard.setPadding((int)(15*dp), (int)(20*dp), (int)(15*dp), (int)(20*dp));
    final android.widget.TextView setTitle = new android.widget.TextView(ctx); setTitle.setTextSize(20f * tScale); setTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); setTitle.setGravity(android.view.Gravity.CENTER); setTitle.setPadding(0, 0, 0, (int)(15*dp)); setCard.addView(setTitle);
    final android.widget.FrameLayout scrollWrapper = new android.widget.FrameLayout(ctx); scrollWrapper.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, 0, 1f));
    final android.widget.ScrollView setScroll = new android.widget.ScrollView(ctx); setScroll.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
    final android.widget.LinearLayout setContainer = new android.widget.LinearLayout(ctx); setContainer.setOrientation(android.widget.LinearLayout.VERTICAL); setScroll.addView(setContainer);
    scrollWrapper.addView(setScroll);
    final android.widget.TextView scrollHint = new android.widget.TextView(ctx); android.widget.FrameLayout.LayoutParams shLp = new android.widget.FrameLayout.LayoutParams(-2, -2); shLp.gravity = android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL; shLp.setMargins(0, 0, 0, (int)(20*dp)); scrollHint.setLayoutParams(shLp); scrollHint.setTextColor(0xFFFFD700); scrollHint.setTextSize(12f * tScale); scrollHint.setTypeface(kurdFont, android.graphics.Typeface.BOLD); scrollHint.setPadding((int)(15*dp), (int)(8*dp), (int)(15*dp), (int)(8*dp)); scrollHint.setVisibility(android.view.View.GONE); scrollWrapper.addView(scrollHint);
    setScroll.setOnTouchListener(new android.view.View.OnTouchListener() { @Override public boolean onTouch(android.view.View v, android.view.MotionEvent event) { try{ if(scrollHint.getVisibility() == android.view.View.VISIBLE && event.getAction() == android.view.MotionEvent.ACTION_MOVE) { scrollHint.animate().alpha(0f).setDuration(200).withEndAction(new Runnable(){ public void run(){ scrollHint.setVisibility(android.view.View.GONE); } }).start(); vaultPrefs.edit().putBoolean("hint_scroll_seen", true).apply(); } }catch(Exception e){} return false; } });
    setCard.addView(scrollWrapper);
    final android.widget.FrameLayout supportLayer = new android.widget.FrameLayout(ctx); supportLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); supportLayer.setBackgroundColor(0xDD000000); supportLayer.setVisibility(android.view.View.GONE); supportLayer.setClickable(true);
    final android.widget.LinearLayout supportCard = new android.widget.LinearLayout(ctx); supportCard.setOrientation(android.widget.LinearLayout.VERTICAL); android.widget.FrameLayout.LayoutParams supCardLp = new android.widget.FrameLayout.LayoutParams(-1, -2); supCardLp.gravity = android.view.Gravity.CENTER; supCardLp.setMargins((int)(25*dp*sScale), 0, (int)(25*dp*sScale), 0); supportCard.setLayoutParams(supCardLp); supportCard.setPadding((int)(20*dp), (int)(25*dp), (int)(20*dp), (int)(25*dp));
    final android.widget.TextView supportTitle = new android.widget.TextView(ctx); supportTitle.setText("پشتیوانی گەشەپێدەر 🎁"); supportTitle.setTextSize(18f * tScale); supportTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); supportTitle.setGravity(android.view.Gravity.CENTER); supportTitle.setPadding(0, 0, 0, (int)(15*dp)); supportCard.addView(supportTitle);
    final android.widget.TextView supportDesc = new android.widget.TextView(ctx); supportDesc.setText("سڵاو، من ناوم (هاوکار)ە، گەنجێکی تەمەن ٢٠ ساڵم. سەدان کاتژمێر لە شەونخونی و ماندووبوونم بەخشی بۆ ئەوەی دێڕ بە دێڕی کۆدەکانی ئەم ئەپە بنووسم، تا نهێنییەکانی تۆ لەوپەڕی ئارامیدا پارێزراو بن. دەمتوانی وەک هەر ئەپێکی تر پڕی بکەم لە ڕیکلامی بێزارکەر، بەڵام نەمویست ئارامیت تێک بدەم. دروستکردنی ئەم جۆرە ئەپانە بە تەنیا کارێکی زۆر قورسە. ئەگەر هەست دەکەیت ئەم بەرنامەیە سوودی بۆت هەبووە، تکایە با ماندووبوونەکەم لەلات بێ نرخ نەبێت. بچووکترین هاوکاریی تۆ، گەورەترین پاڵپشتییە بۆ من تا بتوانم بەردەوام بم و زیاتر خزمەت بکەم."); supportDesc.setTextSize(13f * tScale); supportDesc.setTextColor(0xFFFFFFFF); supportDesc.setTypeface(kurdFont); supportDesc.setGravity(android.view.Gravity.CENTER); supportDesc.setLineSpacing(5f, 1.2f); supportDesc.setPadding(0, 0, 0, (int)(20*dp)); supportCard.addView(supportDesc);
    final android.widget.TextView fastPayBtn = new android.widget.TextView(ctx); fastPayBtn.setText("FastPay: 07756279054"); fastPayBtn.setTextSize(14f * tScale); fastPayBtn.setTextColor(0xFFFFFFFF); fastPayBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); fastPayBtn.setGravity(android.view.Gravity.CENTER); fastPayBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); android.widget.LinearLayout.LayoutParams fpLp = new android.widget.LinearLayout.LayoutParams(-1, -2); fpLp.setMargins(0, 0, 0, (int)(10*dp)); fastPayBtn.setLayoutParams(fpLp); supportCard.addView(fastPayBtn);
    final android.widget.TextView korekBtn = new android.widget.TextView(ctx); korekBtn.setText("Korek: 07505466516"); korekBtn.setTextSize(14f * tScale); korekBtn.setTextColor(0xFFFFFFFF); korekBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); korekBtn.setGravity(android.view.Gravity.CENTER); korekBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); android.widget.LinearLayout.LayoutParams krLp = new android.widget.LinearLayout.LayoutParams(-1, -2); krLp.setMargins(0, 0, 0, (int)(20*dp)); korekBtn.setLayoutParams(krLp); supportCard.addView(korekBtn);
    final android.widget.TextView supportCloseBtn = new android.widget.TextView(ctx); supportCloseBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); supportCloseBtn.setText("گەڕانەوە"); supportCloseBtn.setTextSize(14f * tScale); supportCloseBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); supportCloseBtn.setGravity(android.view.Gravity.CENTER); supportCloseBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); supportCard.addView(supportCloseBtn); supportLayer.addView(supportCard);
    supportBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); supportLayer.setVisibility(android.view.View.VISIBLE); supportLayer.setAlpha(0f); supportCard.setScaleY(0.8f); try{ android.animation.ObjectAnimator.ofFloat(supportLayer, "alpha", 0f, 1f).setDuration(300).start(); android.animation.ObjectAnimator.ofFloat(supportCard, "scaleY", 0.8f, 1f).setDuration(300).start(); }catch(Exception e){} } });
    supportCloseBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); try{ android.animation.ObjectAnimator fade = android.animation.ObjectAnimator.ofFloat(supportLayer, "alpha", 1f, 0f); fade.setDuration(250); fade.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { supportLayer.setVisibility(android.view.View.GONE); }}); fade.start(); }catch(Exception e){} } });
    android.view.View.OnClickListener copyListener = new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); String num = (v == fastPayBtn) ? "07756279054" : "07505466516"; try { android.content.ClipboardManager cb = (android.content.ClipboardManager) ctx.getSystemService(android.content.Context.CLIPBOARD_SERVICE); if(cb!=null) { cb.setPrimaryClip(android.content.ClipData.newPlainText("Donation", num)); android.widget.Toast.makeText(ctx, "ژمارەکە کۆپی کرا ✅", 0).show(); } } catch(Exception e){} } };
    fastPayBtn.setOnClickListener(copyListener); korekBtn.setOnClickListener(copyListener);
    final android.widget.FrameLayout intruderLogLayer = new android.widget.FrameLayout(ctx); intruderLogLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); intruderLogLayer.setBackgroundColor(0xEE05050F); intruderLogLayer.setVisibility(android.view.View.GONE); intruderLogLayer.setClickable(true);
    final android.widget.LinearLayout intruderLogCard = new android.widget.LinearLayout(ctx); intruderLogCard.setOrientation(android.widget.LinearLayout.VERTICAL); intruderLogCard.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); intruderLogCard.setPadding((int)(15*dp), (int)(35*dp), (int)(15*dp), (int)(15*dp)); intruderLogCard.setBackgroundColor(0xEE05050F);
    final android.widget.TextView intruderLogTitle = new android.widget.TextView(ctx); intruderLogTitle.setTextSize(18f * tScale); intruderLogTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); intruderLogTitle.setGravity(android.view.Gravity.CENTER); intruderLogTitle.setPadding(0, 0, 0, (int)(15*dp)); intruderLogCard.addView(intruderLogTitle);
    final android.widget.ScrollView intruderScroll = new android.widget.ScrollView(ctx); intruderScroll.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, 0, 1f)); intruderScroll.setFillViewport(true); intruderScroll.setVerticalScrollBarEnabled(true); intruderScroll.setScrollbarFadingEnabled(true); try { intruderScroll.setOverScrollMode(android.view.View.OVER_SCROLL_ALWAYS); } catch(Exception e) {}
    final android.widget.LinearLayout intruderListContainer = new android.widget.LinearLayout(ctx); intruderListContainer.setOrientation(android.widget.LinearLayout.VERTICAL); intruderListContainer.setPadding(0, 0, 0, (int)(10*dp)); intruderScroll.addView(intruderListContainer); intruderLogCard.addView(intruderScroll);
    final android.widget.TextView intruderCloseBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams icbLp = new android.widget.LinearLayout.LayoutParams(-1, -2); icbLp.setMargins(0, (int)(10*dp), 0, 0); intruderCloseBtn.setLayoutParams(icbLp); intruderCloseBtn.setTextSize(14f * tScale); intruderCloseBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); intruderCloseBtn.setGravity(android.view.Gravity.CENTER); intruderCloseBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); intruderLogCard.addView(intruderCloseBtn); intruderLogLayer.addView(intruderLogCard);
    final android.view.View ilScrollTrack = new android.view.View(ctx); android.widget.FrameLayout.LayoutParams ilTrackLp = new android.widget.FrameLayout.LayoutParams((int)(4*dp), -1); ilTrackLp.gravity = android.view.Gravity.END | android.view.Gravity.TOP; ilTrackLp.setMargins(0, (int)(50*dp), (int)(22*dp), (int)(50*dp)); ilScrollTrack.setLayoutParams(ilTrackLp); ilScrollTrack.setBackgroundColor(0x33FFFFFF); intruderLogLayer.addView(ilScrollTrack);
    final android.view.View ilScrollThumb = new android.view.View(ctx); final android.widget.FrameLayout.LayoutParams ilThumbLp = new android.widget.FrameLayout.LayoutParams((int)(10*dp), (int)(60*dp)); ilThumbLp.gravity = android.view.Gravity.END | android.view.Gravity.TOP; ilThumbLp.setMargins(0, (int)(50*dp), (int)(17*dp), 0); ilScrollThumb.setLayoutParams(ilThumbLp); android.graphics.drawable.GradientDrawable ilThumbBg = new android.graphics.drawable.GradientDrawable(); ilThumbBg.setColor(0xAAFFFFFF); ilThumbBg.setCornerRadius(6f*dp); ilScrollThumb.setBackground(ilThumbBg); intruderLogLayer.addView(ilScrollThumb);
    intruderScroll.getViewTreeObserver().addOnScrollChangedListener(new android.view.ViewTreeObserver.OnScrollChangedListener() { @Override public void onScrollChanged() { try { android.view.View child = intruderScroll.getChildAt(0); if(child == null) return; int trackH = ilScrollTrack.getHeight(); int thumbH = ilThumbLp.height; int maxScroll = child.getHeight() - intruderScroll.getHeight(); if(maxScroll <= 0) return; float ratio = (float)intruderScroll.getScrollY() / maxScroll; int topMargin = (int)(50*dp) + (int)(ratio * (trackH - thumbH)); ilThumbLp.topMargin = topMargin; ilScrollThumb.setLayoutParams(ilThumbLp); } catch(Exception e){} } });
    ilScrollThumb.setOnTouchListener(new android.view.View.OnTouchListener() { float startY = 0; float startScroll = 0; @Override public boolean onTouch(android.view.View v, android.view.MotionEvent e) { try { switch(e.getAction()) { case android.view.MotionEvent.ACTION_DOWN: startY = e.getRawY(); startScroll = intruderScroll.getScrollY(); v.animate().scaleX(2.4f).scaleY(1.2f).setDuration(180).start(); android.graphics.drawable.GradientDrawable tBgD = new android.graphics.drawable.GradientDrawable(); tBgD.setColor(0xFF00DDFF); tBgD.setCornerRadius(6f*dp); v.setBackground(tBgD); break; case android.view.MotionEvent.ACTION_MOVE: float dy = e.getRawY() - startY; android.view.View child2 = intruderScroll.getChildAt(0); if(child2 == null) break; int maxS = child2.getHeight() - intruderScroll.getHeight(); if(maxS <= 0) break; int trackH2 = ilScrollTrack.getHeight(); int thumbH2 = ilThumbLp.height; float ratio2 = dy / (trackH2 - thumbH2); int newScroll = (int)(startScroll + ratio2 * maxS); intruderScroll.smoothScrollTo(0, Math.max(0, Math.min(newScroll, maxS))); break; case android.view.MotionEvent.ACTION_UP: case android.view.MotionEvent.ACTION_CANCEL: v.animate().scaleX(1f).scaleY(1f).setDuration(250).start(); android.graphics.drawable.GradientDrawable tBgU = new android.graphics.drawable.GradientDrawable(); tBgU.setColor(0xAAFFFFFF); tBgU.setCornerRadius(6f*dp); v.setBackground(tBgU); break; } } catch(Exception ex){} return true; } });
    intruderCloseBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); try{ android.animation.ObjectAnimator fade = android.animation.ObjectAnimator.ofFloat(intruderLogLayer, "alpha", 1f, 0f); fade.setDuration(250); fade.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { intruderLogLayer.setVisibility(android.view.View.GONE); }}); fade.start(); }catch(Exception e){} } });
    final Runnable[] refreshIntruderLogHolder = new Runnable[1];
    refreshIntruderLogHolder[0] = new Runnable() {
        @Override public void run() {
            intruderListContainer.removeAllViews();
            try {
                java.io.File intruderDir = new java.io.File(ctx.getFilesDir(), "intruder_log");
                java.io.File[] files = intruderDir.exists() ? intruderDir.listFiles() : null;
                if(files == null || files.length == 0) {
                    android.widget.TextView empty = new android.widget.TextView(ctx); empty.setTextColor(0x88FFFFFF); empty.setTypeface(kurdFont); empty.setGravity(android.view.Gravity.CENTER); empty.setText(tr.get("intruder_empty")); empty.setPadding(0, (int)(50*dp), 0, 0); intruderListContainer.addView(empty); return;
                }
                java.util.Arrays.sort(files, new java.util.Comparator<java.io.File>() { public int compare(java.io.File a, java.io.File b) { return Long.compare(b.lastModified(), a.lastModified()); } });
                for (final java.io.File file : files) {
                    if (!file.getName().endsWith(".jpg")) continue;
                    android.widget.LinearLayout itemRow = new android.widget.LinearLayout(ctx); itemRow.setOrientation(android.widget.LinearLayout.VERTICAL); android.widget.LinearLayout.LayoutParams irLp = new android.widget.LinearLayout.LayoutParams(-1, -2); irLp.setMargins(0, 0, 0, (int)(14*dp)); itemRow.setLayoutParams(irLp); itemRow.setPadding((int)(12*dp), (int)(12*dp), (int)(12*dp), (int)(12*dp)); itemRow.setBackground(ui.glass(0x22FF4444, 0x66FF4444, 14f));
                    android.widget.TextView timeTxt = new android.widget.TextView(ctx); timeTxt.setTextColor(0xFFFF8888); timeTxt.setTextSize(12f * tScale); timeTxt.setTypeface(kurdFont, android.graphics.Typeface.BOLD); timeTxt.setPadding(0, 0, 0, (int)(8*dp));
                    try { long ts = Long.parseLong(file.getName().replace("intruder_", "").replace(".jpg", "")); java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd - HH:mm:ss", java.util.Locale.ENGLISH); timeTxt.setText("⚠️ " + sdf.format(new java.util.Date(ts))); } catch(Exception ex) { timeTxt.setText("⚠️ " + file.getName()); }
                    itemRow.addView(timeTxt);
                    android.widget.ImageView imgView = new android.widget.ImageView(ctx); android.widget.LinearLayout.LayoutParams imgLp = new android.widget.LinearLayout.LayoutParams(-1, (int)(200*dp)); imgView.setLayoutParams(imgLp); imgView.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                    android.graphics.drawable.GradientDrawable imgBg = new android.graphics.drawable.GradientDrawable(); imgBg.setColor(0x33000000); imgBg.setCornerRadius(8f*dp); imgView.setBackground(imgBg);
                    try { android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeFile(file.getAbsolutePath()); if(bmp != null) { android.graphics.Matrix mx = new android.graphics.Matrix(); mx.postRotate(270); android.graphics.Bitmap rotated = android.graphics.Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mx, true); imgView.setImageBitmap(rotated); } } catch(Exception ex) {}
                    itemRow.addView(imgView);
                    android.widget.TextView delPhotoBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams dpLp2 = new android.widget.LinearLayout.LayoutParams(-1, -2); dpLp2.setMargins(0, (int)(8*dp), 0, 0); delPhotoBtn.setLayoutParams(dpLp2); delPhotoBtn.setText("🗑️ سڕینەوە"); delPhotoBtn.setTextColor(0xFFFF6666); delPhotoBtn.setTextSize(12f * tScale); delPhotoBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); delPhotoBtn.setGravity(android.view.Gravity.CENTER); delPhotoBtn.setPadding(0, (int)(10*dp), 0, (int)(10*dp)); delPhotoBtn.setBackground(ui.glass(0x22FF3333, 0x66FF4444, 10f));
                    delPhotoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v2) { file.delete(); if(refreshIntruderLogHolder[0] != null) refreshIntruderLogHolder[0].run(); } });
                    itemRow.addView(delPhotoBtn); intruderListContainer.addView(itemRow);
                }
                if(intruderListContainer.getChildCount() == 0) { android.widget.TextView empty = new android.widget.TextView(ctx); empty.setTextColor(0x88FFFFFF); empty.setTypeface(kurdFont); empty.setGravity(android.view.Gravity.CENTER); empty.setText(tr.get("intruder_empty")); empty.setPadding(0, (int)(50*dp), 0, 0); intruderListContainer.addView(empty); }
            } catch(Exception e) {}
        }
    };
    final Runnable captureIntruderPhoto = new Runnable() {
        @Override public void run() {
            new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
                @Override public void run() {
                    try {
                        if (android.content.pm.PackageManager.PERMISSION_GRANTED != ctx.checkPermission(android.Manifest.permission.CAMERA, android.os.Process.myPid(), android.os.Process.myUid())) return;
                        final android.hardware.camera2.CameraManager cm = (android.hardware.camera2.CameraManager) ctx.getSystemService(android.content.Context.CAMERA_SERVICE);
                        if (cm == null) return;
                        String frontId = null;
                        for (String id : cm.getCameraIdList()) {
                            android.hardware.camera2.CameraCharacteristics ch = cm.getCameraCharacteristics(id);
                            Integer f = ch.get(android.hardware.camera2.CameraCharacteristics.LENS_FACING);
                            if (f != null && f == android.hardware.camera2.CameraCharacteristics.LENS_FACING_FRONT) { frontId = id; break; }
                        }
                        if (frontId == null && cm.getCameraIdList().length > 0) frontId = cm.getCameraIdList()[0];
                        if (frontId == null) return;
                        final String finalCamId = frontId;
                        android.hardware.camera2.CameraCharacteristics chars = cm.getCameraCharacteristics(frontId);
                        android.hardware.camera2.params.StreamConfigurationMap map = chars.get(android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        android.util.Size[] jpegSizes = map != null ? map.getOutputSizes(android.graphics.ImageFormat.JPEG) : null;
                        int capW = 640, capH = 480;
                        if (jpegSizes != null && jpegSizes.length > 0) {
                            android.util.Size best = jpegSizes[0];
                            for (android.util.Size sz : jpegSizes) { if(Math.abs((long)sz.getWidth()*sz.getHeight()-307200L) < Math.abs((long)best.getWidth()*best.getHeight()-307200L)) best = sz; }
                            capW = best.getWidth(); capH = best.getHeight();
                        }
                        final android.media.ImageReader imageReader = android.media.ImageReader.newInstance(capW, capH, android.graphics.ImageFormat.JPEG, 2);
                        final android.graphics.SurfaceTexture surfaceTexture = new android.graphics.SurfaceTexture(10);
                        surfaceTexture.setDefaultBufferSize(capW, capH);
                        final android.view.Surface previewSurface = new android.view.Surface(surfaceTexture);
                        final android.os.Handler bgHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                        cm.openCamera(finalCamId, new android.hardware.camera2.CameraDevice.StateCallback() {
                            @Override public void onOpened(final android.hardware.camera2.CameraDevice camera) {
                                try {
                                    final java.util.List<android.view.Surface> outputs = new java.util.ArrayList<>();
                                    outputs.add(previewSurface); outputs.add(imageReader.getSurface());
                                    camera.createCaptureSession(outputs, new android.hardware.camera2.CameraCaptureSession.StateCallback() {
                                        @Override public void onConfigured(final android.hardware.camera2.CameraCaptureSession session) {
                                            try {
                                                final android.hardware.camera2.CaptureRequest.Builder previewBuilder = camera.createCaptureRequest(android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW);
                                                previewBuilder.addTarget(previewSurface);
                                                previewBuilder.set(android.hardware.camera2.CaptureRequest.FLASH_MODE, android.hardware.camera2.CaptureRequest.FLASH_MODE_OFF);
                                                previewBuilder.set(android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE, android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE_ON);
                                                session.setRepeatingRequest(previewBuilder.build(), null, bgHandler);
                                                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                                                    @Override public void run() {
                                                        try {
                                                            session.stopRepeating();
                                                            imageReader.setOnImageAvailableListener(new android.media.ImageReader.OnImageAvailableListener() {
                                                                @Override public void onImageAvailable(android.media.ImageReader reader) {
                                                                    android.media.Image img = null;
                                                                    try {
                                                                        img = reader.acquireLatestImage();
                                                                        if (img == null) return;
                                                                        java.nio.ByteBuffer buf = img.getPlanes()[0].getBuffer();
                                                                        byte[] data = new byte[buf.remaining()]; buf.get(data);
                                                                        java.io.File dir = new java.io.File(ctx.getFilesDir(), "intruder_log"); dir.mkdirs();
                                                                        java.io.File f2 = new java.io.File(dir, "intruder_" + System.currentTimeMillis() + ".jpg");
                                                                        java.io.FileOutputStream fos = new java.io.FileOutputStream(f2); fos.write(data); fos.flush(); fos.close();
                                                                        new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() { public void run() { android.widget.Toast.makeText(ctx, tr.get("intruder_toast"), android.widget.Toast.LENGTH_SHORT).show(); } });
                                                                    } catch (Exception ex) {
                                                                    } finally {
                                                                        if (img != null) try { img.close(); } catch(Exception ignored){}
                                                                        try { camera.close(); } catch(Exception ignored){}
                                                                        try { previewSurface.release(); } catch(Exception ignored){}
                                                                        try { surfaceTexture.release(); } catch(Exception ignored){}
                                                                        try { imageReader.close(); } catch(Exception ignored){}
                                                                    }
                                                                }
                                                            }, bgHandler);
                                                            final android.hardware.camera2.CaptureRequest.Builder stillBuilder = camera.createCaptureRequest(android.hardware.camera2.CameraDevice.TEMPLATE_STILL_CAPTURE);
                                                            stillBuilder.addTarget(imageReader.getSurface());
                                                            stillBuilder.set(android.hardware.camera2.CaptureRequest.FLASH_MODE, android.hardware.camera2.CaptureRequest.FLASH_MODE_OFF);
                                                            stillBuilder.set(android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE, android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE_ON);
                                                            stillBuilder.set(android.hardware.camera2.CaptureRequest.JPEG_QUALITY, (byte)85);
                                                            session.capture(stillBuilder.build(), null, bgHandler);
                                                        } catch (Exception ex) { try { camera.close(); previewSurface.release(); surfaceTexture.release(); imageReader.close(); } catch(Exception ignored){} }
                                                    }
                                                }, 800);
                                            } catch (Exception ex) { try { camera.close(); previewSurface.release(); surfaceTexture.release(); imageReader.close(); } catch(Exception ignored){} }
                                        }
                                        @Override public void onConfigureFailed(android.hardware.camera2.CameraCaptureSession session) { try { camera.close(); previewSurface.release(); surfaceTexture.release(); imageReader.close(); } catch(Exception ignored){} }
                                    }, bgHandler);
                                } catch (Exception ex) { try { camera.close(); previewSurface.release(); surfaceTexture.release(); imageReader.close(); } catch(Exception ignored){} }
                            }
                            @Override public void onDisconnected(android.hardware.camera2.CameraDevice camera) { try { camera.close(); previewSurface.release(); surfaceTexture.release(); imageReader.close(); } catch(Exception ignored){} }
                            @Override public void onError(android.hardware.camera2.CameraDevice camera, int error) { try { camera.close(); previewSurface.release(); surfaceTexture.release(); imageReader.close(); } catch(Exception ignored){} }
                        }, bgHandler);
                    } catch (Exception e) {}
                }
            });
        }
    };
    final android.widget.FrameLayout readModeLayer = new android.widget.FrameLayout(ctx); readModeLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); readModeLayer.setBackgroundColor(0xF4040410); readModeLayer.setVisibility(android.view.View.GONE); readModeLayer.setClickable(true);
    final android.widget.LinearLayout readModeCard = new android.widget.LinearLayout(ctx); readModeCard.setOrientation(android.widget.LinearLayout.VERTICAL); readModeCard.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); readModeCard.setPadding((int)(20*dp), (int)(40*dp), (int)(20*dp), (int)(20*dp)); readModeCard.setBackgroundColor(0xF4040410);
    final android.widget.TextView readModeTitle = new android.widget.TextView(ctx); readModeTitle.setTextSize(16f * tScale); readModeTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); readModeTitle.setGravity(android.view.Gravity.CENTER); readModeTitle.setSingleLine(true); readModeTitle.setEllipsize(android.text.TextUtils.TruncateAt.END); android.widget.LinearLayout.LayoutParams rmtLp = new android.widget.LinearLayout.LayoutParams(-1, -2); rmtLp.setMargins(0, 0, 0, (int)(12*dp)); readModeTitle.setLayoutParams(rmtLp); readModeCard.addView(readModeTitle);
    final android.widget.ScrollView readModeScroll = new android.widget.ScrollView(ctx); readModeScroll.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, 0, 1f)); readModeScroll.setFillViewport(true); readModeScroll.setVerticalScrollBarEnabled(true); readModeScroll.setScrollbarFadingEnabled(true); try { readModeScroll.setOverScrollMode(android.view.View.OVER_SCROLL_ALWAYS); } catch(Exception e) {}
    final android.widget.TextView readModeContent = new android.widget.TextView(ctx); readModeContent.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); readModeContent.setTextSize(18f * tScale); readModeContent.setTextColor(0xFFEEEEEE); readModeContent.setTypeface(kurdFont); readModeContent.setLineSpacing(6f, 1.4f); readModeContent.setPadding((int)(8*dp), (int)(8*dp), (int)(55*dp), (int)(20*dp)); readModeContent.setTextIsSelectable(true); readModeScroll.addView(readModeContent); readModeCard.addView(readModeScroll);
    final android.widget.TextView readModeCloseBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams rmcLp = new android.widget.LinearLayout.LayoutParams(-1, -2); rmcLp.setMargins(0, (int)(15*dp), 0, 0); readModeCloseBtn.setLayoutParams(rmcLp); readModeCloseBtn.setTextSize(15f * tScale); readModeCloseBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); readModeCloseBtn.setGravity(android.view.Gravity.CENTER); readModeCloseBtn.setPadding(0, (int)(14*dp), 0, (int)(14*dp)); readModeCard.addView(readModeCloseBtn); readModeLayer.addView(readModeCard);
    final android.view.View rmScrollTrack = new android.view.View(ctx); android.widget.FrameLayout.LayoutParams rmTrackLp = new android.widget.FrameLayout.LayoutParams((int)(4*dp), -1); rmTrackLp.gravity = android.view.Gravity.END | android.view.Gravity.TOP; rmTrackLp.setMargins(0, (int)(60*dp), (int)(22*dp), (int)(70*dp)); rmScrollTrack.setLayoutParams(rmTrackLp); rmScrollTrack.setBackgroundColor(0x33FFFFFF); readModeLayer.addView(rmScrollTrack);
    final android.view.View rmScrollThumb = new android.view.View(ctx); final android.widget.FrameLayout.LayoutParams rmThumbLp = new android.widget.FrameLayout.LayoutParams((int)(10*dp), (int)(70*dp)); rmThumbLp.gravity = android.view.Gravity.END | android.view.Gravity.TOP; rmThumbLp.setMargins(0, (int)(60*dp), (int)(17*dp), 0); rmScrollThumb.setLayoutParams(rmThumbLp); android.graphics.drawable.GradientDrawable rmThumbBg = new android.graphics.drawable.GradientDrawable(); rmThumbBg.setColor(0xAAFFFFFF); rmThumbBg.setCornerRadius(6f*dp); rmScrollThumb.setBackground(rmThumbBg); readModeLayer.addView(rmScrollThumb);
    readModeScroll.getViewTreeObserver().addOnScrollChangedListener(new android.view.ViewTreeObserver.OnScrollChangedListener() { @Override public void onScrollChanged() { try { android.view.View child = readModeScroll.getChildAt(0); if(child == null) return; int trackH = rmScrollTrack.getHeight(); int thumbH = rmThumbLp.height; int maxScroll = child.getHeight() - readModeScroll.getHeight(); if(maxScroll <= 0) return; float ratio = (float)readModeScroll.getScrollY() / maxScroll; int topMargin = (int)(60*dp) + (int)(ratio * (trackH - thumbH)); rmThumbLp.topMargin = topMargin; rmScrollThumb.setLayoutParams(rmThumbLp); } catch(Exception e){} } });
    rmScrollThumb.setOnTouchListener(new android.view.View.OnTouchListener() { float startY = 0; float startScroll = 0; @Override public boolean onTouch(android.view.View v, android.view.MotionEvent e) { try { switch(e.getAction()) { case android.view.MotionEvent.ACTION_DOWN: startY = e.getRawY(); startScroll = readModeScroll.getScrollY(); v.animate().scaleX(2.4f).scaleY(1.2f).setDuration(180).start(); android.graphics.drawable.GradientDrawable tBgD = new android.graphics.drawable.GradientDrawable(); tBgD.setColor(0xFF00DDFF); tBgD.setCornerRadius(6f*dp); v.setBackground(tBgD); break; case android.view.MotionEvent.ACTION_MOVE: float dy = e.getRawY() - startY; android.view.View child2 = readModeScroll.getChildAt(0); if(child2 == null) break; int maxS = child2.getHeight() - readModeScroll.getHeight(); if(maxS <= 0) break; int trackH2 = rmScrollTrack.getHeight(); int thumbH2 = rmThumbLp.height; float ratio2 = dy / (trackH2 - thumbH2); int newScroll = (int)(startScroll + ratio2 * maxS); readModeScroll.smoothScrollTo(0, Math.max(0, Math.min(newScroll, maxS))); break; case android.view.MotionEvent.ACTION_UP: case android.view.MotionEvent.ACTION_CANCEL: v.animate().scaleX(1f).scaleY(1f).setDuration(200).start(); android.graphics.drawable.GradientDrawable tBgU = new android.graphics.drawable.GradientDrawable(); tBgU.setColor(0xAAFFFFFF); tBgU.setCornerRadius(6f*dp); v.setBackground(tBgU); break; } } catch(Exception ex){} return true; } });
    readModeCloseBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); try{ android.animation.ObjectAnimator fade = android.animation.ObjectAnimator.ofFloat(readModeLayer, "alpha", 1f, 0f); fade.setDuration(250); fade.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { readModeLayer.setVisibility(android.view.View.GONE); }}); fade.start(); }catch(Exception e){} } });
    final java.util.ArrayList<Runnable> uiUpdaters = new java.util.ArrayList<>();
    class SetHelper {
        public android.widget.LinearLayout createToggle(String subKey, final String prefKey, final String descKey, final android.widget.TextView[] lblOut, final android.widget.TextView[] subOut) {
            android.widget.LinearLayout row = new android.widget.LinearLayout(ctx); row.setOrientation(android.widget.LinearLayout.HORIZONTAL); android.widget.LinearLayout.LayoutParams rLp = new android.widget.LinearLayout.LayoutParams(-1, -2); rLp.setMargins(0, 0, 0, (int)(10*dp)); row.setLayoutParams(rLp); row.setGravity(android.view.Gravity.CENTER_VERTICAL); row.setPadding((int)(15*dp), (int)(12*dp), (int)(15*dp), (int)(12*dp)); row.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 12f));
            android.widget.LinearLayout textCol = new android.widget.LinearLayout(ctx); textCol.setOrientation(android.widget.LinearLayout.VERTICAL); textCol.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
            android.widget.TextView lbl = new android.widget.TextView(ctx); lbl.setTextSize(14f * tScale); lbl.setTextColor(0xFFFFFFFF); lbl.setTypeface(kurdFont); lblOut[0] = lbl;
            android.widget.TextView sub = new android.widget.TextView(ctx); sub.setTextSize(10f * tScale); sub.setTextColor(0xFFAAAAAA); sub.setTypeface(kurdFont); sub.setPadding(0, (int)(2*dp), 0, 0); subOut[0] = sub;
            textCol.addView(lbl); textCol.addView(sub);
            android.widget.TextView infoBtn = new android.widget.TextView(ctx); infoBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); infoBtn.setText("ℹ️"); infoBtn.setTextSize(18f * tScale); infoBtn.setPadding((int)(5*dp), 0, (int)(15*dp), 0);
            final String finalSubKey = subKey; infoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); currentInfo[0] = descKey; currentInfo[1] = finalSubKey; showInfoDialog.run(); } });
            final android.widget.TextView toggle = new android.widget.TextView(ctx); toggle.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); toggle.setPadding((int)(10*dp), (int)(5*dp), (int)(10*dp), (int)(5*dp)); toggle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); toggle.setTextSize(11f * tScale);
            row.addView(textCol); row.addView(infoBtn); row.addView(toggle);
            uiUpdaters.add(new Runnable() { public void run() { boolean isOn = vaultPrefs.getBoolean(prefKey, false); if(isOn) { toggle.setText(tr.get("active")); toggle.setTextColor(0xFF22FF22); toggle.setBackground(ui.glass(0x2200FF00, 0xFF22FF22, 20f)); } else { toggle.setText(tr.get("inactive")); toggle.setTextColor(0xFFFF4444); toggle.setBackground(ui.glass(0x22FF0000, 0xFFFF4444, 20f)); } } });
            row.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); vaultPrefs.edit().putBoolean(prefKey, !vaultPrefs.getBoolean(prefKey, false)).apply(); for(Runnable r:uiUpdaters) r.run(); } });
            return row;
        }
        public android.widget.TextView createHeader(String text) { android.widget.TextView h = new android.widget.TextView(ctx); h.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); h.setText(text); h.setTextColor(0xFFAAAAAA); h.setTextSize(12f * tScale); h.setTypeface(kurdFont); h.setPadding((int)(5*dp), (int)(15*dp), 0, (int)(5*dp)); return h; }
    }
    SetHelper sHelp = new SetHelper();
    final android.widget.TextView vaultDesignHeader = sHelp.createHeader(""); setContainer.addView(vaultDesignHeader);
    android.widget.LinearLayout vaultDesignRow = new android.widget.LinearLayout(ctx); vaultDesignRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); android.widget.LinearLayout.LayoutParams vdRowLp = new android.widget.LinearLayout.LayoutParams(-1, -2); vdRowLp.setMargins(0, 0, 0, (int)(10*dp)); vaultDesignRow.setLayoutParams(vdRowLp); vaultDesignRow.setGravity(android.view.Gravity.CENTER_VERTICAL); vaultDesignRow.setPadding((int)(15*dp), (int)(14*dp), (int)(15*dp), (int)(14*dp)); vaultDesignRow.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 12f));
    android.widget.LinearLayout vdTextCol = new android.widget.LinearLayout(ctx); vdTextCol.setOrientation(android.widget.LinearLayout.VERTICAL); vdTextCol.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
    final android.widget.TextView vdLbl = new android.widget.TextView(ctx); vdLbl.setTextSize(14f * tScale); vdLbl.setTextColor(0xFFFFFFFF); vdLbl.setTypeface(kurdFont);
    final android.widget.TextView vdSub = new android.widget.TextView(ctx); vdSub.setTextSize(10f * tScale); vdSub.setTextColor(0xFFAAAAAA); vdSub.setTypeface(kurdFont); vdSub.setPadding(0, (int)(2*dp), 0, 0);
    vdTextCol.addView(vdLbl); vdTextCol.addView(vdSub); vaultDesignRow.addView(vdTextCol);
    final android.widget.TextView btnDesignClassic = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams bdcLp = new android.widget.LinearLayout.LayoutParams(-2, -2); bdcLp.setMargins((int)(6*dp), 0, 0, 0); btnDesignClassic.setLayoutParams(bdcLp); btnDesignClassic.setPadding((int)(10*dp), (int)(6*dp), (int)(10*dp), (int)(6*dp)); btnDesignClassic.setTextSize(11f * tScale); btnDesignClassic.setTypeface(kurdFont);
    final android.widget.TextView btnDesignModern = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams bdmLp = new android.widget.LinearLayout.LayoutParams(-2, -2); bdmLp.setMargins((int)(6*dp), 0, 0, 0); btnDesignModern.setLayoutParams(bdmLp); btnDesignModern.setPadding((int)(10*dp), (int)(6*dp), (int)(10*dp), (int)(6*dp)); btnDesignModern.setTextSize(11f * tScale); btnDesignModern.setTypeface(kurdFont);
    vaultDesignRow.addView(btnDesignClassic); vaultDesignRow.addView(btnDesignModern); setContainer.addView(vaultDesignRow);
    btnDesignClassic.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); vaultPrefs.edit().putInt("pref_vault_design", 0).apply(); for(Runnable r : uiUpdaters) r.run(); } });
    btnDesignModern.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); vaultPrefs.edit().putInt("pref_vault_design", 1).apply(); for(Runnable r : uiUpdaters) r.run(); } });
    uiUpdaters.add(new Runnable() { public void run() { int curDesign = vaultPrefs.getInt("pref_vault_design", 0); int mColor = ui.getMainColor(); if(curDesign == 0) { btnDesignClassic.setBackground(ui.glass(0x44FFFFFF, mColor, 8f)); btnDesignClassic.setTextColor(mColor); btnDesignModern.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 8f)); btnDesignModern.setTextColor(0xFFAAAAAA); } else if(curDesign == 1) { btnDesignModern.setBackground(ui.glass(0x44FFFFFF, mColor, 8f)); btnDesignModern.setTextColor(mColor); btnDesignClassic.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 8f)); btnDesignClassic.setTextColor(0xFFAAAAAA); } else { btnDesignClassic.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 8f)); btnDesignClassic.setTextColor(0xFFAAAAAA); btnDesignModern.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 8f)); btnDesignModern.setTextColor(0xFFAAAAAA); } } });
    final android.widget.TextView proDesignHeader = sHelp.createHeader(""); setContainer.addView(proDesignHeader);
    android.widget.LinearLayout proDesignRow = new android.widget.LinearLayout(ctx); proDesignRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); android.widget.LinearLayout.LayoutParams pdRowLp = new android.widget.LinearLayout.LayoutParams(-1, -2); pdRowLp.setMargins(0, 0, 0, (int)(10*dp)); proDesignRow.setLayoutParams(pdRowLp); proDesignRow.setGravity(android.view.Gravity.CENTER_VERTICAL); proDesignRow.setPadding((int)(15*dp), (int)(14*dp), (int)(15*dp), (int)(14*dp)); proDesignRow.setBackground(ui.glass(0x22000000, 0x55FFD700, 12f));
    android.widget.LinearLayout pdTextCol = new android.widget.LinearLayout(ctx); pdTextCol.setOrientation(android.widget.LinearLayout.VERTICAL); pdTextCol.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
    final android.widget.TextView pdLbl = new android.widget.TextView(ctx); pdLbl.setTextSize(14f * tScale); pdLbl.setTextColor(0xFFFFD700); pdLbl.setTypeface(kurdFont, android.graphics.Typeface.BOLD);
    final android.widget.TextView pdSub = new android.widget.TextView(ctx); pdSub.setTextSize(10f * tScale); pdSub.setTextColor(0xFFAAAAAA); pdSub.setTypeface(kurdFont); pdSub.setPadding(0, (int)(2*dp), 0, 0);
    pdTextCol.addView(pdLbl); pdTextCol.addView(pdSub); proDesignRow.addView(pdTextCol);
    final android.widget.TextView btnDesignFirefly = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams bdfLp = new android.widget.LinearLayout.LayoutParams(-2, -2); bdfLp.setMargins((int)(6*dp), 0, 0, 0); btnDesignFirefly.setLayoutParams(bdfLp); btnDesignFirefly.setPadding((int)(10*dp), (int)(6*dp), (int)(10*dp), (int)(6*dp)); btnDesignFirefly.setTextSize(11f * tScale); btnDesignFirefly.setTypeface(kurdFont);
    final android.widget.TextView btnDesignParadise = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams bdpLp = new android.widget.LinearLayout.LayoutParams(-2, -2); bdpLp.setMargins((int)(6*dp), 0, 0, 0); btnDesignParadise.setLayoutParams(bdpLp); btnDesignParadise.setPadding((int)(10*dp), (int)(6*dp), (int)(10*dp), (int)(6*dp)); btnDesignParadise.setTextSize(11f * tScale); btnDesignParadise.setTypeface(kurdFont);
    proDesignRow.addView(btnDesignFirefly); proDesignRow.addView(btnDesignParadise); setContainer.addView(proDesignRow);
    android.widget.LinearLayout autoRow = new android.widget.LinearLayout(ctx); autoRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); android.widget.LinearLayout.LayoutParams arLp = new android.widget.LinearLayout.LayoutParams(-1, -2); arLp.setMargins(0, 0, 0, (int)(10*dp)); autoRow.setLayoutParams(arLp); autoRow.setGravity(android.view.Gravity.CENTER_VERTICAL); autoRow.setPadding((int)(15*dp), (int)(12*dp), (int)(15*dp), (int)(12*dp)); autoRow.setBackground(ui.glass(0x22000000, 0x44FFFFFF, 12f));
    android.widget.LinearLayout autoTextCol = new android.widget.LinearLayout(ctx); autoTextCol.setOrientation(android.widget.LinearLayout.VERTICAL); autoTextCol.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
    final android.widget.TextView autoLbl = new android.widget.TextView(ctx); autoLbl.setTextSize(14f * tScale); autoLbl.setTextColor(0xFFFFFFFF); autoLbl.setTypeface(kurdFont); autoTextCol.addView(autoLbl);
    final android.widget.TextView autoSub = new android.widget.TextView(ctx); autoSub.setTextSize(10f * tScale); autoSub.setTextColor(0xFFAAAAAA); autoSub.setTypeface(kurdFont); autoSub.setPadding(0,(int)(2*dp),0,0); autoTextCol.addView(autoSub);
    autoRow.addView(autoTextCol);
    android.widget.TextView autoInfo = new android.widget.TextView(ctx); autoInfo.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2,-2)); autoInfo.setText("ℹ️"); autoInfo.setTextSize(18f * tScale); autoInfo.setPadding((int)(5*dp),0,(int)(10*dp),0);
    autoInfo.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); currentInfo[0]="auto_theme_desc"; currentInfo[1]="auto_theme_sub"; showInfoDialog.run(); } });
    autoRow.addView(autoInfo);
    final android.widget.TextView autoToggle = new android.widget.TextView(ctx); autoToggle.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2,-2)); autoToggle.setPadding((int)(10*dp),(int)(5*dp),(int)(10*dp),(int)(5*dp)); autoToggle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); autoToggle.setTextSize(11f * tScale); autoRow.addView(autoToggle); setContainer.addView(autoRow);
    uiUpdaters.add(new Runnable() { public void run() { autoLbl.setText(tr.get("auto_theme_title")); autoSub.setText(tr.get("auto_theme_sub")); boolean on = vaultPrefs.getBoolean("pref_auto_theme",false); if(on){autoToggle.setText(tr.get("active")); autoToggle.setTextColor(0xFF22FF22); autoToggle.setBackground(ui.glass(0x2200FF00,0xFF22FF22,20f));}else{autoToggle.setText(tr.get("inactive")); autoToggle.setTextColor(0xFFFF4444); autoToggle.setBackground(ui.glass(0x22FF0000,0xFFFF4444,20f));} } });
    autoRow.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); boolean newVal = !vaultPrefs.getBoolean("pref_auto_theme",false); vaultPrefs.edit().putBoolean("pref_auto_theme",newVal).apply(); if(newVal){ int hour=java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY); vaultPrefs.edit().putInt("pref_vault_design", hour>=6&&hour<18?3:2).apply(); } for(Runnable r:uiUpdaters) r.run(); } });
    btnDesignFirefly.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); vaultPrefs.edit().putInt("pref_vault_design", 2).putBoolean("pref_auto_theme",false).apply(); for(Runnable r : uiUpdaters) r.run(); } });
    btnDesignParadise.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); vaultPrefs.edit().putInt("pref_vault_design", 3).putBoolean("pref_auto_theme",false).apply(); for(Runnable r : uiUpdaters) r.run(); } });
    uiUpdaters.add(new Runnable() { public void run() { int curDesign = vaultPrefs.getInt("pref_vault_design", 0); if(curDesign == 2) { btnDesignFirefly.setBackground(ui.glass(0x44FFD700, 0xFFFFD700, 8f)); btnDesignFirefly.setTextColor(0xFFFFD700); btnDesignParadise.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 8f)); btnDesignParadise.setTextColor(0xFFAAAAAA); } else if(curDesign == 3) { btnDesignParadise.setBackground(ui.glass(0x44FF69B4, 0xFFFF69B4, 8f)); btnDesignParadise.setTextColor(0xFFFF69B4); btnDesignFirefly.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 8f)); btnDesignFirefly.setTextColor(0xFFAAAAAA); } else { btnDesignFirefly.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 8f)); btnDesignFirefly.setTextColor(0xFFAAAAAA); btnDesignParadise.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 8f)); btnDesignParadise.setTextColor(0xFFAAAAAA); } } });
    setContainer.addView(sHelp.createHeader("🎨 Design"));
    final android.widget.TextView[] lblColor = new android.widget.TextView[1]; final android.widget.TextView[] subColor = new android.widget.TextView[1];
    android.widget.LinearLayout colorRow = new android.widget.LinearLayout(ctx); colorRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); android.widget.LinearLayout.LayoutParams crLp = new android.widget.LinearLayout.LayoutParams(-1, -2); crLp.setMargins(0, 0, 0, (int)(10*dp)); colorRow.setLayoutParams(crLp); colorRow.setGravity(android.view.Gravity.CENTER_VERTICAL); colorRow.setPadding((int)(15*dp), (int)(10*dp), (int)(15*dp), (int)(10*dp)); colorRow.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 12f));
    android.widget.LinearLayout cTextCol = new android.widget.LinearLayout(ctx); cTextCol.setOrientation(android.widget.LinearLayout.VERTICAL); cTextCol.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
    android.widget.TextView cLbl = new android.widget.TextView(ctx); cLbl.setTextSize(14f * tScale); cLbl.setTextColor(0xFFFFFFFF); cLbl.setTypeface(kurdFont); lblColor[0] = cLbl;
    android.widget.TextView cSub = new android.widget.TextView(ctx); cSub.setTextSize(10f * tScale); cSub.setTextColor(0xFFAAAAAA); cSub.setTypeface(kurdFont); cSub.setPadding(0, (int)(2*dp), 0, 0); subColor[0] = cSub; cTextCol.addView(cLbl); cTextCol.addView(cSub); colorRow.addView(cTextCol);
    android.widget.TextView cInfoBtn = new android.widget.TextView(ctx); cInfoBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); cInfoBtn.setText("ℹ️"); cInfoBtn.setTextSize(18f * tScale); cInfoBtn.setPadding((int)(5*dp), 0, (int)(10*dp), 0); cInfoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); currentInfo[0] = "desc_color"; currentInfo[1] = "sub_color"; showInfoDialog.run(); } }); colorRow.addView(cInfoBtn);
    final int[] pColors = {0xFF00F0FF, 0xFFFF007F, 0xFF00FF00, 0xFFFFD700, 0xFFBF5AF2}; final android.view.View[] cDots = new android.view.View[5];
    for(int i=0; i<5; i++) { final int cVal = pColors[i]; android.view.View dot = new android.view.View(ctx); android.widget.LinearLayout.LayoutParams dLp = new android.widget.LinearLayout.LayoutParams((int)(24*dp), (int)(24*dp)); dLp.setMargins((int)(5*dp), 0, 0, 0); dot.setLayoutParams(dLp); cDots[i] = dot; dot.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); vaultPrefs.edit().putInt("pref_main_color", cVal).apply(); for(Runnable r:uiUpdaters) r.run(); } }); colorRow.addView(dot); }
    uiUpdaters.add(new Runnable() { public void run() { int cur = ui.getMainColor(); for(int i=0; i<5; i++) { android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(); gd.setShape(android.graphics.drawable.GradientDrawable.OVAL); gd.setColor(pColors[i]); if(cur == pColors[i]) gd.setStroke((int)(2*dp), 0xFFFFFFFF); else gd.setStroke(0,0); cDots[i].setBackground(gd); } } });
    setContainer.addView(colorRow);
    final android.widget.TextView[] lblStyle = new android.widget.TextView[1]; final android.widget.TextView[] subStyle = new android.widget.TextView[1];
    android.widget.LinearLayout styleRow = new android.widget.LinearLayout(ctx); styleRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); styleRow.setLayoutParams(crLp); styleRow.setGravity(android.view.Gravity.CENTER_VERTICAL); styleRow.setPadding((int)(15*dp), (int)(10*dp), (int)(15*dp), (int)(10*dp)); styleRow.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 12f));
    android.widget.LinearLayout sTextCol = new android.widget.LinearLayout(ctx); sTextCol.setOrientation(android.widget.LinearLayout.VERTICAL); sTextCol.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
    android.widget.TextView sLbl = new android.widget.TextView(ctx); sLbl.setTextSize(14f * tScale); sLbl.setTextColor(0xFFFFFFFF); sLbl.setTypeface(kurdFont); lblStyle[0] = sLbl;
    android.widget.TextView sSub = new android.widget.TextView(ctx); sSub.setTextSize(10f * tScale); sSub.setTextColor(0xFFAAAAAA); sSub.setTypeface(kurdFont); sSub.setPadding(0, (int)(2*dp), 0, 0); subStyle[0] = sSub; sTextCol.addView(sLbl); sTextCol.addView(sSub); styleRow.addView(sTextCol);
    android.widget.TextView sInfoBtn = new android.widget.TextView(ctx); sInfoBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); sInfoBtn.setText("ℹ️"); sInfoBtn.setTextSize(18f * tScale); sInfoBtn.setPadding((int)(5*dp), 0, (int)(10*dp), 0); sInfoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); currentInfo[0] = "desc_style"; currentInfo[1] = "sub_style"; showInfoDialog.run(); } }); styleRow.addView(sInfoBtn);
    final android.widget.TextView btnGlass = new android.widget.TextView(ctx); final android.widget.TextView btnMin = new android.widget.TextView(ctx);
    final android.widget.TextView[] sBtns = {btnGlass, btnMin}; final int[] sVals = {0, 2};
    for(int i=0; i<2; i++) { sBtns[i].setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); sBtns[i].setPadding((int)(8*dp), (int)(4*dp), (int)(8*dp), (int)(4*dp)); sBtns[i].setTextSize(10f * tScale); sBtns[i].setTypeface(kurdFont); android.widget.LinearLayout.LayoutParams slLp = (android.widget.LinearLayout.LayoutParams)sBtns[i].getLayoutParams(); slLp.setMargins((int)(4*dp), 0, 0, 0); final int sVal = sVals[i]; sBtns[i].setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); vaultPrefs.edit().putInt("pref_card_style", sVal).apply(); for(Runnable r:uiUpdaters) r.run(); }}); styleRow.addView(sBtns[i]); }
    uiUpdaters.add(new Runnable() { public void run() { int cur = vaultPrefs.getInt("pref_card_style", 0); for(int i=0; i<2; i++) { if(cur==sVals[i]) { sBtns[i].setBackground(ui.glass(0x44FFFFFF, ui.getMainColor(), 8f)); sBtns[i].setTextColor(ui.getMainColor()); } else { sBtns[i].setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 8f)); sBtns[i].setTextColor(0xFFAAAAAA); } } } });
    setContainer.addView(styleRow);
    setContainer.addView(sHelp.createHeader("🔒 Security"));
    android.widget.LinearLayout intruderToggleRow = new android.widget.LinearLayout(ctx); intruderToggleRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); android.widget.LinearLayout.LayoutParams itrLp = new android.widget.LinearLayout.LayoutParams(-1, -2); itrLp.setMargins(0, 0, 0, (int)(10*dp)); intruderToggleRow.setLayoutParams(itrLp); intruderToggleRow.setGravity(android.view.Gravity.CENTER_VERTICAL); intruderToggleRow.setPadding((int)(15*dp), (int)(12*dp), (int)(15*dp), (int)(12*dp)); intruderToggleRow.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 12f));
    android.widget.LinearLayout intruderTextCol = new android.widget.LinearLayout(ctx); intruderTextCol.setOrientation(android.widget.LinearLayout.VERTICAL); intruderTextCol.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
    final android.widget.TextView intruderToggleLbl = new android.widget.TextView(ctx); intruderToggleLbl.setTextSize(14f * tScale); intruderToggleLbl.setTextColor(0xFFFFFFFF); intruderToggleLbl.setTypeface(kurdFont);
    final android.widget.TextView intruderToggleSub = new android.widget.TextView(ctx); intruderToggleSub.setTextSize(10f * tScale); intruderToggleSub.setTextColor(0xFFAAAAAA); intruderToggleSub.setTypeface(kurdFont); intruderToggleSub.setPadding(0, (int)(2*dp), 0, 0);
    intruderTextCol.addView(intruderToggleLbl); intruderTextCol.addView(intruderToggleSub);
    android.widget.TextView intruderInfoBtn = new android.widget.TextView(ctx); intruderInfoBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); intruderInfoBtn.setText("ℹ️"); intruderInfoBtn.setTextSize(18f * tScale); intruderInfoBtn.setPadding((int)(8*dp), (int)(8*dp), (int)(12*dp), (int)(8*dp));
    intruderInfoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); currentInfo[0] = "desc_intruder"; currentInfo[1] = "[ Silent Front Camera ]"; showInfoDialog.run(); } });
    final android.widget.TextView intruderToggleBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams itbLp2 = new android.widget.LinearLayout.LayoutParams(-2, -2); intruderToggleBtn.setLayoutParams(itbLp2); intruderToggleBtn.setPadding((int)(10*dp), (int)(5*dp), (int)(10*dp), (int)(5*dp)); intruderToggleBtn.setGravity(android.view.Gravity.CENTER); intruderToggleBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); intruderToggleBtn.setTextSize(11f * tScale);
    intruderToggleRow.addView(intruderTextCol); intruderToggleRow.addView(intruderInfoBtn); intruderToggleRow.addView(intruderToggleBtn); setContainer.addView(intruderToggleRow);
    uiUpdaters.add(new Runnable() { public void run() { boolean on = vaultPrefs.getBoolean("pref_intruder_enabled", false); if(on) { intruderToggleBtn.setText(tr.get("active")); intruderToggleBtn.setTextColor(0xFF22FF22); intruderToggleBtn.setBackground(ui.glass(0x2200FF00, 0xFF22FF22, 20f)); } else { intruderToggleBtn.setText(tr.get("inactive")); intruderToggleBtn.setTextColor(0xFFFF4444); intruderToggleBtn.setBackground(ui.glass(0x22FF0000, 0xFFFF4444, 20f)); } } });
    intruderToggleRow.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30);
    boolean camOk = (android.content.pm.PackageManager.PERMISSION_GRANTED == ctx.checkPermission(android.Manifest.permission.CAMERA, android.os.Process.myPid(), android.os.Process.myUid()));
    if(!camOk && !vaultPrefs.getBoolean("pref_intruder_enabled", false)) {
        try {
            final android.widget.FrameLayout camDlgLayer = new android.widget.FrameLayout(ctx);
            camDlgLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
            camDlgLayer.setBackgroundColor(0xCC000000);
            camDlgLayer.setClickable(true);
            android.widget.LinearLayout camCard = new android.widget.LinearLayout(ctx);
            camCard.setOrientation(android.widget.LinearLayout.VERTICAL);
            android.widget.FrameLayout.LayoutParams ccLp = new android.widget.FrameLayout.LayoutParams(-1, -2);
            ccLp.gravity = android.view.Gravity.CENTER;
            ccLp.setMargins((int)(30*dp), 0, (int)(30*dp), 0);
            camCard.setLayoutParams(ccLp);
            camCard.setPadding((int)(25*dp), (int)(30*dp), (int)(25*dp), (int)(25*dp));
            camCard.setBackground(ui.glass(0xEE14142B, ui.getMainColor(), 20f));
            android.widget.TextView camIcon = new android.widget.TextView(ctx);
            camIcon.setText("📸"); camIcon.setTextSize(42f * tScale);
            camIcon.setGravity(android.view.Gravity.CENTER);
            android.widget.LinearLayout.LayoutParams ciLp = new android.widget.LinearLayout.LayoutParams(-1, -2);
            ciLp.setMargins(0, 0, 0, (int)(12*dp)); camIcon.setLayoutParams(ciLp);
            camCard.addView(camIcon);
            android.widget.TextView camTitle = new android.widget.TextView(ctx);
            camTitle.setText(tr.get("cam_perm_title")); camTitle.setTextSize(18f * tScale);
            camTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD);
            camTitle.setTextColor(ui.getMainColor()); camTitle.setGravity(android.view.Gravity.CENTER);
            android.widget.LinearLayout.LayoutParams ctLp = new android.widget.LinearLayout.LayoutParams(-1, -2);
            ctLp.setMargins(0, 0, 0, (int)(14*dp)); camTitle.setLayoutParams(ctLp);
            camCard.addView(camTitle);
            android.widget.TextView camMsg = new android.widget.TextView(ctx);
            camMsg.setText(tr.get("cam_perm_msg"));
            camMsg.setTextSize(14f * tScale); camMsg.setTextColor(0xFFDDDDDD);
            camMsg.setTypeface(kurdFont); camMsg.setGravity(android.view.Gravity.CENTER);
            camMsg.setLineSpacing(5f, 1.25f);
            android.widget.LinearLayout.LayoutParams cmLp = new android.widget.LinearLayout.LayoutParams(-1, -2);
            cmLp.setMargins(0, 0, 0, (int)(24*dp)); camMsg.setLayoutParams(cmLp);
            camCard.addView(camMsg);
            android.widget.LinearLayout camBtnRow = new android.widget.LinearLayout(ctx);
            camBtnRow.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            camBtnRow.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2));
            android.widget.TextView camNo = new android.widget.TextView(ctx);
            android.widget.LinearLayout.LayoutParams cnLp = new android.widget.LinearLayout.LayoutParams(0, -2, 1f);
            cnLp.setMargins(0, 0, (int)(8*dp), 0); camNo.setLayoutParams(cnLp);
            camNo.setText(tr.get("cam_perm_no")); camNo.setTextSize(14f * tScale);
            camNo.setTypeface(kurdFont, android.graphics.Typeface.BOLD);
            camNo.setGravity(android.view.Gravity.CENTER); camNo.setPadding(0, (int)(12*dp), 0, (int)(12*dp));
            camNo.setBackground(ui.glass(0x22FFFFFF, 0x55FFFFFF, 12f)); camNo.setTextColor(0xFFFFFFFF);
            android.widget.TextView camYes = new android.widget.TextView(ctx);
            camYes.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
            camYes.setText(tr.get("cam_perm_yes")); camYes.setTextSize(14f * tScale);
            camYes.setTypeface(kurdFont, android.graphics.Typeface.BOLD);
            camYes.setGravity(android.view.Gravity.CENTER); camYes.setPadding(0, (int)(12*dp), 0, (int)(12*dp));
            camYes.setBackground(ui.glass(0x22000000 | (ui.getMainColor() & 0xFFFFFF), ui.getMainColor(), 12f));
            camYes.setTextColor(ui.getMainColor());
            camBtnRow.addView(camNo); camBtnRow.addView(camYes);
            camCard.addView(camBtnRow);
            camDlgLayer.addView(camCard);
            ((android.view.ViewGroup)settingsLayer).addView(camDlgLayer);
            camDlgLayer.setAlpha(0f); camCard.setScaleX(0.85f); camCard.setScaleY(0.85f);
            camDlgLayer.animate().alpha(1f).setDuration(300).start();
            camCard.animate().scaleX(1f).scaleY(1f).setDuration(300).setInterpolator(new android.view.animation.OvershootInterpolator(1.2f)).start();
            final Runnable closeCamDlg = new Runnable() { public void run() {
                try { camDlgLayer.animate().alpha(0f).setDuration(220).withEndAction(new Runnable() { public void run() { try { ((android.view.ViewGroup)settingsLayer).removeView(camDlgLayer); } catch(Exception e2){} } }).start(); } catch(Exception e){}
            }};
            camNo.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); closeCamDlg.run(); } });
            camYes.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); closeCamDlg.run();
                new android.os.Handler().postDelayed(new Runnable() { public void run() {
                    try { android.content.Intent intent = new android.content.Intent("android.settings.action.MANAGE_APP_PERMISSIONS"); intent.putExtra("android.intent.extra.PACKAGE_NAME", ctx.getPackageName()); intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK); ctx.startActivity(intent); } catch(Exception e2) {
                    try { android.content.Intent i2 = new android.content.Intent("android.intent.action.APP_PERMISSIONS"); i2.putExtra("android.intent.extra.PACKAGE_NAME", ctx.getPackageName()); i2.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK); ctx.startActivity(i2); } catch(Exception e3) {
                    try { android.content.Intent i3 = new android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS); i3.setData(android.net.Uri.parse("package:" + ctx.getPackageName())); i3.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK); ctx.startActivity(i3); } catch(Exception e4){} } }
                }}, 250);
            } });
        } catch(Exception ex) {}
    } else {
        vaultPrefs.edit().putBoolean("pref_intruder_enabled", !vaultPrefs.getBoolean("pref_intruder_enabled", false)).apply();
        for(Runnable r:uiUpdaters) r.run();
    }
} });
    final android.widget.TextView intruderLogBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams ilbLp = new android.widget.LinearLayout.LayoutParams(-1, -2); ilbLp.setMargins(0, 0, 0, (int)(10*dp)); intruderLogBtn.setLayoutParams(ilbLp); intruderLogBtn.setTextSize(13f * tScale); intruderLogBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); intruderLogBtn.setGravity(android.view.Gravity.CENTER); intruderLogBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); intruderLogBtn.setText("🕵️ " + tr.get("intruder_log_btn")); intruderLogBtn.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 12f));
    setContainer.addView(intruderLogBtn);
    intruderLogBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); intruderLogTitle.setText(tr.get("intruder_log_title")); intruderLogTitle.setTextColor(ui.getMainColor()); intruderCloseBtn.setText(tr.get("back")); if(refreshIntruderLogHolder[0] != null) refreshIntruderLogHolder[0].run(); intruderLogLayer.setVisibility(android.view.View.VISIBLE); intruderLogLayer.setAlpha(0f); try{ android.animation.ObjectAnimator.ofFloat(intruderLogLayer, "alpha", 0f, 1f).setDuration(300).start(); }catch(Exception e){} } });
    uiUpdaters.add(new Runnable() { public void run() { int mColor = ui.getMainColor(); intruderLogBtn.setTextColor(mColor); } });
    final android.widget.TextView[] lblShake = new android.widget.TextView[1]; final android.widget.TextView[] subShake = new android.widget.TextView[1];
    setContainer.addView(sHelp.createToggle("sub_shake", "pref_shake", "desc_shake", lblShake, subShake));
    final android.widget.TextView[] lblSecure = new android.widget.TextView[1]; final android.widget.TextView[] subSecure = new android.widget.TextView[1];
    setContainer.addView(sHelp.createToggle("sub_secure", "pref_secure_screen", "desc_secure", lblSecure, subSecure));
    uiUpdaters.add(new Runnable() { public void run() { try { if (vaultPrefs.getBoolean("pref_secure_screen", false)) { MainActivity.this.getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE); } else { MainActivity.this.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE); } } catch(Exception e){} }});
    final android.widget.TextView[] lblLock = new android.widget.TextView[1]; final android.widget.TextView[] subLock = new android.widget.TextView[1];
    android.widget.LinearLayout lockRow = new android.widget.LinearLayout(ctx); lockRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); lockRow.setLayoutParams(crLp); lockRow.setGravity(android.view.Gravity.CENTER_VERTICAL); lockRow.setPadding((int)(15*dp), (int)(10*dp), (int)(15*dp), (int)(10*dp)); lockRow.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 12f));
    android.widget.LinearLayout lTextCol = new android.widget.LinearLayout(ctx); lTextCol.setOrientation(android.widget.LinearLayout.VERTICAL); lTextCol.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
    android.widget.TextView lkLbl = new android.widget.TextView(ctx); lkLbl.setTextSize(14f * tScale); lkLbl.setTextColor(0xFFFFFFFF); lkLbl.setTypeface(kurdFont); lblLock[0] = lkLbl;
    android.widget.TextView lSub = new android.widget.TextView(ctx); lSub.setTextSize(10f * tScale); lSub.setTextColor(0xFFAAAAAA); lSub.setTypeface(kurdFont); lSub.setPadding(0, (int)(2*dp), 0, 0); subLock[0] = lSub; lTextCol.addView(lkLbl); lTextCol.addView(lSub); lockRow.addView(lTextCol);
    android.widget.TextView lInfoBtn = new android.widget.TextView(ctx); lInfoBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); lInfoBtn.setText("ℹ️"); lInfoBtn.setTextSize(18f * tScale); lInfoBtn.setPadding((int)(5*dp), 0, (int)(10*dp), 0); lInfoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); currentInfo[0] = "desc_lock"; currentInfo[1] = "sub_lock"; showInfoDialog.run(); } }); lockRow.addView(lInfoBtn);
    final android.widget.TextView[] lkBtns = new android.widget.TextView[4]; final int[] lkVals = {30, 60, 300, 0};
    for(int i=0; i<4; i++) { lkBtns[i] = new android.widget.TextView(ctx); lkBtns[i].setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); lkBtns[i].setPadding((int)(8*dp), (int)(4*dp), (int)(8*dp), (int)(4*dp)); lkBtns[i].setTextSize(10f * tScale); lkBtns[i].setTypeface(kurdFont); android.widget.LinearLayout.LayoutParams llLp = (android.widget.LinearLayout.LayoutParams)lkBtns[i].getLayoutParams(); llLp.setMargins((int)(4*dp), 0, 0, 0); final int lVal = lkVals[i]; lkBtns[i].setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); vaultPrefs.edit().putInt("pref_auto_lock", lVal).apply(); for(Runnable r:uiUpdaters) r.run(); }}); lockRow.addView(lkBtns[i]); }
    uiUpdaters.add(new Runnable() { public void run() { int cur = vaultPrefs.getInt("pref_auto_lock", 0); for(int i=0; i<4; i++) { if(cur==lkVals[i]) { lkBtns[i].setBackground(ui.glass(0x44FFFFFF, ui.getMainColor(), 8f)); lkBtns[i].setTextColor(ui.getMainColor()); } else { lkBtns[i].setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 8f)); lkBtns[i].setTextColor(0xFFAAAAAA); } } } });
    setContainer.addView(lockRow);
    final android.widget.TextView[] lblDt = new android.widget.TextView[1]; final android.widget.TextView[] subDt = new android.widget.TextView[1];
    setContainer.addView(sHelp.createToggle("dt_sub", "pref_dt_enabled", "dt_desc", lblDt, subDt));
    final android.widget.LinearLayout dtContainer = new android.widget.LinearLayout(ctx); dtContainer.setOrientation(android.widget.LinearLayout.VERTICAL); dtContainer.setLayoutParams(crLp); dtContainer.setPadding((int)(15*dp), (int)(15*dp), (int)(15*dp), (int)(15*dp)); dtContainer.setBackground(ui.glass(0x22000000, 0x44FFFFFF, 12f)); setContainer.addView(dtContainer);
    final android.widget.TextView dtTimeLabel = new android.widget.TextView(ctx); dtTimeLabel.setTextSize(12f * tScale); dtTimeLabel.setTextColor(0xFFFFFFFF); dtTimeLabel.setTypeface(kurdFont); dtTimeLabel.setPadding(0, 0, 0, (int)(12*dp)); dtContainer.addView(dtTimeLabel);
    android.widget.LinearLayout dtCustomTimeRow = new android.widget.LinearLayout(ctx); dtCustomTimeRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); android.widget.LinearLayout.LayoutParams dtRowLp = new android.widget.LinearLayout.LayoutParams(-1, -2); dtRowLp.setMargins(0, 0, 0, (int)(15*dp)); dtCustomTimeRow.setLayoutParams(dtRowLp);
    final android.widget.EditText dtHoursInput = new android.widget.EditText(ctx); android.widget.LinearLayout.LayoutParams hrLp = new android.widget.LinearLayout.LayoutParams(0, -2, 1f); hrLp.setMargins(0, 0, (int)(10*dp), 0); dtHoursInput.setLayoutParams(hrLp); dtHoursInput.setTextColor(0xFFFFFFFF); dtHoursInput.setTextSize(14f * tScale); dtHoursInput.setTypeface(kurdFont); dtHoursInput.setPadding((int)(12*dp), (int)(12*dp), (int)(12*dp), (int)(12*dp)); dtHoursInput.setSingleLine(true); dtHoursInput.setHint("کاتژمێر..."); dtHoursInput.setHintTextColor(0x77FFFFFF); dtHoursInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER); dtHoursInput.setBackground(ui.glass(0x33000000, 0x55FFFFFF, 8f)); dtHoursInput.setGravity(android.view.Gravity.CENTER); dtCustomTimeRow.addView(dtHoursInput);
    final android.widget.EditText dtMinsInput = new android.widget.EditText(ctx); android.widget.LinearLayout.LayoutParams minLp = new android.widget.LinearLayout.LayoutParams(0, -2, 1f); dtMinsInput.setLayoutParams(minLp); dtMinsInput.setTextColor(0xFFFFFFFF); dtMinsInput.setTextSize(14f * tScale); dtMinsInput.setTypeface(kurdFont); dtMinsInput.setPadding((int)(12*dp), (int)(12*dp), (int)(12*dp), (int)(12*dp)); dtMinsInput.setSingleLine(true); dtMinsInput.setHint("خولەک..."); dtMinsInput.setHintTextColor(0x77FFFFFF); dtMinsInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER); dtMinsInput.setBackground(ui.glass(0x33000000, 0x55FFFFFF, 8f)); dtMinsInput.setGravity(android.view.Gravity.CENTER); dtCustomTimeRow.addView(dtMinsInput);
    dtContainer.addView(dtCustomTimeRow);
    final android.widget.TextView dtSaveTimeBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams dsLp = new android.widget.LinearLayout.LayoutParams(-1, -2); dsLp.setMargins(0, 0, 0, (int)(15*dp)); dtSaveTimeBtn.setLayoutParams(dsLp); dtSaveTimeBtn.setPadding((int)(12*dp), (int)(12*dp), (int)(12*dp), (int)(12*dp)); dtSaveTimeBtn.setText("💾 خەزنکردنی کات"); dtSaveTimeBtn.setTextSize(12f * tScale); dtSaveTimeBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); dtSaveTimeBtn.setGravity(android.view.Gravity.CENTER);
    dtSaveTimeBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); tools.hideKeyboard(dtHoursInput); tools.hideKeyboard(dtMinsInput); long h = 0; long m = 0; try { if(!dtHoursInput.getText().toString().isEmpty()) h = Long.parseLong(dtHoursInput.getText().toString().trim()); } catch(Exception e){} try { if(!dtMinsInput.getText().toString().isEmpty()) m = Long.parseLong(dtMinsInput.getText().toString().trim()); } catch(Exception e){} long totalMillis = (h * 3600000L) + (m * 60000L); if (totalMillis > 0) { vaultPrefs.edit().putLong("pref_dt_limit", totalMillis).putInt("pref_dt_action", 0).apply(); android.widget.Toast.makeText(ctx, "کاتەکە بە سەرکەوتوویی خەزن کرا!", 0).show(); for(Runnable r:uiUpdaters) r.run(); } else { android.widget.Toast.makeText(ctx, "تکایە کاتێکی دروست بنووسە!", 0).show(); } }}); dtContainer.addView(dtSaveTimeBtn);
    final android.widget.TextView dtActWipe = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams aLp = new android.widget.LinearLayout.LayoutParams(-1, -2); aLp.setMargins(0, 0, 0, (int)(8*dp)); dtActWipe.setLayoutParams(aLp); dtActWipe.setPadding((int)(12*dp), (int)(10*dp), (int)(12*dp), (int)(10*dp)); dtActWipe.setTextSize(12f * tScale); dtActWipe.setTypeface(kurdFont, android.graphics.Typeface.BOLD); dtContainer.addView(dtActWipe);
    final android.widget.TextView dtWarningNote = new android.widget.TextView(ctx); dtWarningNote.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); dtWarningNote.setTextSize(11f * tScale); dtWarningNote.setTextColor(0xFFFFD700); dtWarningNote.setTypeface(kurdFont); dtWarningNote.setPadding(0, (int)(5*dp), 0, 0); dtContainer.addView(dtWarningNote);
    uiUpdaters.add(new Runnable() { public void run() { boolean dtEnabled = vaultPrefs.getBoolean("pref_dt_enabled", false); dtContainer.setVisibility(dtEnabled ? android.view.View.VISIBLE : android.view.View.GONE); if(dtEnabled) { long curL = vaultPrefs.getLong("pref_dt_limit", 0); if(curL > 0) { long h = curL / 3600000L; long m = (curL % 3600000L) / 60000L; dtHoursInput.setText(h > 0 ? String.valueOf(h) : ""); dtMinsInput.setText(m > 0 ? String.valueOf(m) : ""); } dtSaveTimeBtn.setBackground(ui.glass(0x22FFFFFF, 0x55FFFFFF, 8f)); dtSaveTimeBtn.setTextColor(0xFFFFFFFF); dtActWipe.setBackground(ui.glass(0x44FFFFFF, ui.getMainColor(), 8f)); dtActWipe.setTextColor(ui.getMainColor()); } }});

    setContainer.addView(sHelp.createHeader("💾 Data & System"));
    final android.widget.TextView tutRedoBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams rLp = new android.widget.LinearLayout.LayoutParams(-1, -2); rLp.setMargins(0, 0, 0, (int)(10*dp)); tutRedoBtn.setLayoutParams(rLp); tutRedoBtn.setTextSize(14f * tScale); tutRedoBtn.setTextColor(0xFF00F0FF); tutRedoBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); tutRedoBtn.setGravity(android.view.Gravity.CENTER); tutRedoBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); tutRedoBtn.setBackground(ui.glass(0x1100F0FF, 0xFF00F0FF, 12f)); setContainer.addView(tutRedoBtn);
    android.widget.LinearLayout actRow1 = new android.widget.LinearLayout(ctx); actRow1.setOrientation(android.widget.LinearLayout.HORIZONTAL); actRow1.setLayoutParams(rLp); actRow1.setGravity(android.view.Gravity.CENTER_VERTICAL); actRow1.setPadding((int)(15*dp), (int)(12*dp), (int)(15*dp), (int)(12*dp)); actRow1.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 12f));
    android.widget.LinearLayout a1TextCol = new android.widget.LinearLayout(ctx); a1TextCol.setOrientation(android.widget.LinearLayout.VERTICAL); a1TextCol.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
    final android.widget.TextView lblExp = new android.widget.TextView(ctx); lblExp.setTextSize(14f * tScale); lblExp.setTextColor(0xFF00F0FF); lblExp.setTypeface(kurdFont, android.graphics.Typeface.BOLD);
    final android.widget.TextView subExp = new android.widget.TextView(ctx); subExp.setTextSize(10f * tScale); subExp.setTextColor(0xFFAAAAAA); subExp.setTypeface(kurdFont); subExp.setPadding(0, (int)(2*dp), 0, 0); a1TextCol.addView(lblExp); a1TextCol.addView(subExp); actRow1.addView(a1TextCol);
    android.widget.TextView a1InfoBtn = new android.widget.TextView(ctx); a1InfoBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); a1InfoBtn.setText("ℹ️"); a1InfoBtn.setTextSize(18f * tScale); a1InfoBtn.setPadding((int)(5*dp), 0, 0, 0); a1InfoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); currentInfo[0] = "desc_exp"; currentInfo[1] = "sub_exp"; showInfoDialog.run(); } }); actRow1.addView(a1InfoBtn); setContainer.addView(actRow1);
    android.widget.LinearLayout actRow2 = new android.widget.LinearLayout(ctx); actRow2.setOrientation(android.widget.LinearLayout.HORIZONTAL); actRow2.setLayoutParams(rLp); actRow2.setGravity(android.view.Gravity.CENTER_VERTICAL); actRow2.setPadding((int)(15*dp), (int)(12*dp), (int)(15*dp), (int)(12*dp)); actRow2.setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 12f));
    android.widget.LinearLayout a2TextCol = new android.widget.LinearLayout(ctx); a2TextCol.setOrientation(android.widget.LinearLayout.VERTICAL); a2TextCol.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f));
    final android.widget.TextView lblImp = new android.widget.TextView(ctx); lblImp.setTextSize(14f * tScale); lblImp.setTextColor(0xFFFF9F0A); lblImp.setTypeface(kurdFont, android.graphics.Typeface.BOLD);
    final android.widget.TextView subImp = new android.widget.TextView(ctx); subImp.setTextSize(10f * tScale); subImp.setTextColor(0xFFAAAAAA); subImp.setTypeface(kurdFont); subImp.setPadding(0, (int)(2*dp), 0, 0); a2TextCol.addView(lblImp); a2TextCol.addView(subImp); actRow2.addView(a2TextCol);
    android.widget.TextView a2InfoBtn = new android.widget.TextView(ctx); a2InfoBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); a2InfoBtn.setText("ℹ️"); a2InfoBtn.setTextSize(18f * tScale); a2InfoBtn.setPadding((int)(5*dp), 0, 0, 0); a2InfoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); currentInfo[0] = "desc_imp"; currentInfo[1] = "sub_imp"; showInfoDialog.run(); } }); actRow2.addView(a2InfoBtn); setContainer.addView(actRow2);
    final android.widget.TextView lblDelAll = new android.widget.TextView(ctx); lblDelAll.setLayoutParams(rLp); lblDelAll.setTextSize(14f * tScale); lblDelAll.setTextColor(0xFFFF4444); lblDelAll.setTypeface(kurdFont, android.graphics.Typeface.BOLD); lblDelAll.setGravity(android.view.Gravity.CENTER); lblDelAll.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); lblDelAll.setBackground(ui.glass(0x11FF4444, 0xFFFF4444, 12f)); setContainer.addView(lblDelAll);
    setContainer.addView(sHelp.createHeader("🌍 Language"));
    final android.widget.LinearLayout langRow = new android.widget.LinearLayout(ctx); langRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); android.widget.LinearLayout.LayoutParams langRLp = new android.widget.LinearLayout.LayoutParams(-1, -2); langRLp.setMargins(0, 0, 0, (int)(12*dp)); langRow.setLayoutParams(langRLp);  langRow.setPadding((int)(8*dp), (int)(8*dp), (int)(8*dp), (int)(8*dp)); setContainer.addView(langRow);
    final android.widget.TextView btnKu = new android.widget.TextView(ctx); final android.widget.TextView btnEn = new android.widget.TextView(ctx); final android.widget.TextView btnAr = new android.widget.TextView(ctx);
    final android.widget.TextView[] langBtns = {btnKu, btnEn, btnAr}; final String[] langCodes = {"ku", "en", "ar"}; final String[] langNames = {"کوردی", "English", "العربية"};
    android.view.View.OnClickListener langClick = new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); if(v == btnKu) vaultPrefs.edit().putString("pref_lang", "ku").apply(); else if(v == btnEn) vaultPrefs.edit().putString("pref_lang", "en").apply(); else if(v == btnAr) vaultPrefs.edit().putString("pref_lang", "ar").apply(); for(Runnable r:uiUpdaters) r.run(); }};
    for(int i=0; i<3; i++) { langBtns[i].setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f)); langBtns[i].setText(langNames[i]); langBtns[i].setTextSize(14f * tScale); langBtns[i].setTypeface(kurdFont); langBtns[i].setGravity(android.view.Gravity.CENTER); langBtns[i].setPadding(0, (int)(12*dp), 0, (int)(12*dp)); android.widget.LinearLayout.LayoutParams llp = (android.widget.LinearLayout.LayoutParams)langBtns[i].getLayoutParams(); llp.setMargins((int)(2*dp), 0, (int)(2*dp), 0); langBtns[i].setOnClickListener(langClick); langRow.addView(langBtns[i]); }
    uiUpdaters.add(new Runnable() { public void run() { String cur = vaultPrefs.getString("pref_lang", "ku"); for(int i=0; i<3; i++) { if(cur.equals(langCodes[i])) { langBtns[i].setBackground(ui.glass(0x44FFFFFF, ui.getMainColor(), 12f)); langBtns[i].setTextColor(ui.getMainColor()); } else { langBtns[i].setBackground(ui.glass(0x11FFFFFF, 0x33FFFFFF, 12f)); langBtns[i].setTextColor(0xFFAAAAAA); } } } });
    final android.widget.TextView setCloseBtn = new android.widget.TextView(ctx); setCloseBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); setCloseBtn.setTextSize(14f * tScale); setCloseBtn.setTextColor(0xFFFFFFFF); setCloseBtn.setTypeface(kurdFont); setCloseBtn.setGravity(android.view.Gravity.CENTER); setCloseBtn.setPadding(0, (int)(15*dp), 0, (int)(15*dp)); setCard.addView(setCloseBtn); settingsLayer.addView(setCard);
    final android.widget.FrameLayout delLayer = new android.widget.FrameLayout(ctx); delLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); delLayer.setBackgroundColor(0xDD000000); delLayer.setVisibility(android.view.View.GONE); delLayer.setClickable(true);
    final android.widget.LinearLayout delCard = new android.widget.LinearLayout(ctx); delCard.setOrientation(android.widget.LinearLayout.VERTICAL); android.widget.FrameLayout.LayoutParams dCardLp = new android.widget.FrameLayout.LayoutParams(-1, -2); dCardLp.gravity = android.view.Gravity.CENTER; dCardLp.setMargins((int)(30*dp*sScale), 0, (int)(30*dp*sScale), 0); delCard.setLayoutParams(dCardLp); delCard.setPadding((int)(25*dp), (int)(30*dp), (int)(25*dp), (int)(30*dp));
    final android.widget.TextView delTitle = new android.widget.TextView(ctx); delTitle.setTextSize(20f * tScale); delTitle.setTextColor(0xFFFF4444); delTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); delTitle.setGravity(android.view.Gravity.CENTER); delTitle.setPadding(0, 0, 0, (int)(15*dp)); delCard.addView(delTitle);
    final android.widget.TextView delMsg = new android.widget.TextView(ctx); delMsg.setTextSize(14f * tScale); delMsg.setTextColor(0xFFFFFFFF); delMsg.setTypeface(kurdFont); delMsg.setGravity(android.view.Gravity.CENTER); delMsg.setPadding(0, 0, 0, (int)(25*dp)); delCard.addView(delMsg);
    final android.widget.LinearLayout delBtnRow = new android.widget.LinearLayout(ctx); delBtnRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); delBtnRow.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2));
    final android.widget.TextView delNoBtn = new android.widget.TextView(ctx); delNoBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f)); delNoBtn.setTextSize(14f * tScale); delNoBtn.setTextColor(0xFFFFFFFF); delNoBtn.setTypeface(kurdFont); delNoBtn.setGravity(android.view.Gravity.CENTER); delNoBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); delBtnRow.addView(delNoBtn);
    final android.widget.TextView delYesBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams dyLp = new android.widget.LinearLayout.LayoutParams(0, -2, 1f); dyLp.setMargins((int)(10*dp), 0, 0, 0); delYesBtn.setLayoutParams(dyLp); delYesBtn.setTextSize(14f * tScale); delYesBtn.setTextColor(0xFFFF4444); delYesBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); delYesBtn.setGravity(android.view.Gravity.CENTER); delYesBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); delBtnRow.addView(delYesBtn);
    delCard.addView(delBtnRow); delLayer.addView(delCard);
    final android.widget.FrameLayout hint9Layer = new android.widget.FrameLayout(ctx); android.widget.FrameLayout.LayoutParams h9Lp = new android.widget.FrameLayout.LayoutParams(-1, -2); h9Lp.gravity = android.view.Gravity.TOP | android.view.Gravity.CENTER_HORIZONTAL; h9Lp.setMargins((int)(20*dp*sScale), (int)(80*dp), (int)(20*dp*sScale), 0); hint9Layer.setLayoutParams(h9Lp); hint9Layer.setVisibility(android.view.View.GONE);
    final android.widget.LinearLayout hint9Card = new android.widget.LinearLayout(ctx); hint9Card.setOrientation(android.widget.LinearLayout.VERTICAL); hint9Card.setBackground(ui.glass(0xEE14142B, 0xFFFFFFFF, 15f)); hint9Card.setPadding((int)(15*dp), (int)(15*dp), (int)(15*dp), (int)(15*dp)); try { if (android.os.Build.VERSION.SDK_INT >= 21) hint9Card.setElevation(20f); } catch(Exception e){}
    final android.widget.TextView hint9TxtKu = new android.widget.TextView(ctx); hint9TxtKu.setTextColor(0xFFFFFFFF); hint9TxtKu.setTypeface(kurdFont, android.graphics.Typeface.BOLD); hint9TxtKu.setTextSize(14f * tScale); hint9TxtKu.setGravity(android.view.Gravity.CENTER);
    final android.widget.TextView hint9TxtEn = new android.widget.TextView(ctx); hint9TxtEn.setTextColor(0xFFAAAAAA); hint9TxtEn.setTypeface(kurdFont); hint9TxtEn.setTextSize(11f * tScale); hint9TxtEn.setGravity(android.view.Gravity.CENTER); hint9TxtEn.setPadding(0, (int)(5*dp), 0, 0);
    hint9Card.addView(hint9TxtKu); hint9Card.addView(hint9TxtEn); hint9Layer.addView(hint9Card);
    final android.widget.LinearLayout fakeUiLayer = new android.widget.LinearLayout(ctx); fakeUiLayer.setOrientation(android.widget.LinearLayout.VERTICAL); fakeUiLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); fakeUiLayer.setBackgroundColor(0xFF1E1E1E);
    final android.widget.TextView calcSubDisplay = new android.widget.TextView(ctx); calcSubDisplay.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, 0, 0.6f)); calcSubDisplay.setText(""); calcSubDisplay.setTextSize(22f * tScale); calcSubDisplay.setTextColor(0x88AAAAAA); calcSubDisplay.setGravity(android.view.Gravity.BOTTOM | android.view.Gravity.END); calcSubDisplay.setPadding((int)(20*dp), (int)(10*dp), (int)(30*dp), (int)(8*dp)); fakeUiLayer.addView(calcSubDisplay);
    final android.widget.TextView calcDisplay = new android.widget.TextView(ctx); calcDisplay.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, 0, 1f)); calcDisplay.setText("0"); calcDisplay.setTextSize(60f * tScale); calcDisplay.setTypeface(kurdFont); calcDisplay.setTextColor(0xFFFFFFFF); calcDisplay.setGravity(android.view.Gravity.BOTTOM | android.view.Gravity.END); calcDisplay.setPadding((int)(20*dp), 0, (int)(30*dp), (int)(20*dp)); fakeUiLayer.addView(calcDisplay);
    final double[] cNum = {0};
    final String[] cOp = {""};
    final boolean[] cWaitNew = {false};
    final boolean[] cHasResult = {false};
    final String[][] calcBtnsTxt = {{"7","8","9","÷"},{"4","5","6","×"},{"1","2","3","−"},{"AC","0",".","+"},{"%","00","=","←"}};
    final android.widget.TextView[] btn9Holder = new android.widget.TextView[1]; final android.widget.TextView[] btn3Holder = new android.widget.TextView[1];
    for (int i = 0; i < 5; i++) {
        final int iF = i;
        android.widget.LinearLayout rowLinear = new android.widget.LinearLayout(ctx); rowLinear.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        rowLinear.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, (int)(80*dp*sScale)));
        for (int j = 0; j < 4; j++) {
            final int jF = j; final String tTxt = calcBtnsTxt[iF][jF];
            android.widget.TextView btnCalc = new android.widget.TextView(ctx);
            android.widget.LinearLayout.LayoutParams btnLp = new android.widget.LinearLayout.LayoutParams(0, -1, 1f);
            btnLp.setMargins((int)(4*dp),(int)(4*dp),(int)(4*dp),(int)(4*dp)); btnCalc.setLayoutParams(btnLp);
            btnCalc.setText(tTxt); btnCalc.setTextSize(26f * tScale); btnCalc.setTypeface(null, android.graphics.Typeface.BOLD);
            boolean isOp = tTxt.equals("÷")||tTxt.equals("×")||tTxt.equals("−")||tTxt.equals("+");
            boolean isSpec = tTxt.equals("AC")||tTxt.equals("←")||tTxt.equals("%");
            boolean isEq = tTxt.equals("=");
            int btnColor = isEq ? 0xFFFF9F0A : isOp ? 0xFF505050 : isSpec ? 0xFF636363 : 0xFF333333;
            int txtColor = isEq ? 0xFFFFFFFF : isOp ? 0xFFFF9F0A : 0xFFFFFFFF;
            android.graphics.drawable.GradientDrawable calcBtnBg = new android.graphics.drawable.GradientDrawable();
            calcBtnBg.setColor(btnColor); calcBtnBg.setCornerRadius(40f*dp); btnCalc.setBackground(calcBtnBg);
            btnCalc.setTextColor(txtColor); btnCalc.setGravity(android.view.Gravity.CENTER);
            rowLinear.addView(btnCalc);
            if(tTxt.equals("9")) btn9Holder[0] = btnCalc; if(tTxt.equals("3")) btn3Holder[0] = btnCalc;
            btnCalc.setOnTouchListener(new android.view.View.OnTouchListener() {
                public boolean onTouch(android.view.View v, android.view.MotionEvent e) {
                    if(e.getAction()==android.view.MotionEvent.ACTION_DOWN) { v.animate().scaleX(0.88f).scaleY(0.88f).setDuration(80).start(); }
                    else if(e.getAction()==android.view.MotionEvent.ACTION_UP||e.getAction()==android.view.MotionEvent.ACTION_CANCEL) { v.animate().scaleX(1f).scaleY(1f).setDuration(120).start(); }
                    return false;
                }
            });
            btnCalc.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(android.view.View v) {
                    tools.vibe(15);
                    String cur = calcDisplay.getText().toString();
                    if(cur.length() > 14) cur = cur.substring(0,14);
                    if(tTxt.equals("AC")) {
                        calcDisplay.setText("0"); calcSubDisplay.setText(""); cNum[0]=0; cOp[0]=""; cWaitNew[0]=false; cHasResult[0]=false;
                    } else if(tTxt.equals("←")) {
                        if(cur.length() <= 1 || (cur.length()==2 && cur.startsWith("-"))) { calcDisplay.setText("0"); }
                        else { calcDisplay.setText(cur.substring(0, cur.length()-1)); }
                    } else if(tTxt.equals("%")) {
                        try { double v2 = Double.parseDouble(cur); calcDisplay.setText(fmtR(v2/100)); cHasResult[0]=true; } catch(Exception ex){}
                    } else if(tTxt.equals("÷")||tTxt.equals("×")||tTxt.equals("−")||tTxt.equals("+")) {
                        try {
                            double parsed = Double.parseDouble(cur);
                            if(!cOp[0].isEmpty() && !cWaitNew[0]) {
                                double res = calc(cNum[0], parsed, cOp[0]);
                                cNum[0] = res; calcDisplay.setText(fmtR(res));
                            } else { cNum[0] = parsed; }
                        } catch(Exception ex){ cNum[0] = 0; }
                        cOp[0] = tTxt; cWaitNew[0] = true; cHasResult[0] = false;
                        calcSubDisplay.setText(fmtR(cNum[0]) + " " + cOp[0]);
                    } else if(tTxt.equals("=")) {
                        try {
                            double parsed = Double.parseDouble(cur);
                            if(!cOp[0].isEmpty()) {
                                calcSubDisplay.setText(fmtR(cNum[0]) + " " + cOp[0] + " " + fmtR(parsed) + " =");
                                double res = calc(cNum[0], parsed, cOp[0]);
                                calcDisplay.setText(fmtR(res));
                                cNum[0] = res; cOp[0] = ""; cWaitNew[0] = true; cHasResult[0] = true;
                            }
                        } catch(Exception ex){}
                    } else {
                        if(cWaitNew[0] || cHasResult[0]) {
                            calcDisplay.setText(tTxt.equals(".")?"0.":tTxt);
                            cWaitNew[0] = false; cHasResult[0] = false;
                        } else if(tTxt.equals(".")) {
                            if(!cur.contains(".")) calcDisplay.setText(cur + ".");
                        } else if(tTxt.equals("00")) {
                            if(!cur.equals("0")) calcDisplay.setText(cur.length()<14?cur+"00":cur);
                        } else {
                            if(cur.equals("0")) calcDisplay.setText(tTxt);
                            else if(cur.length() < 14) calcDisplay.setText(cur + tTxt);
                        }
                    }
                }
                private double calc(double a, double b, String op) {
                    if(op.equals("+")) return a+b;
                    if(op.equals("−")) return a-b;
                    if(op.equals("×")) return a*b;
                    if(op.equals("÷")) return b==0?0:a/b;
                    return b;
                }
                private String fmtR(double n) {
                    if(Double.isNaN(n)||Double.isInfinite(n)) return "خەڵەت";
                    if(n==(long)n && Math.abs(n)<1e13) return String.valueOf((long)n);
                    String s = String.format("%.10f", n).replaceAll("0+$","").replaceAll("\\.$","");
                    if(s.length()>14) s=s.substring(0,14);
                    return s;
                }
            });
        } fakeUiLayer.addView(rowLinear);
    } android.view.View bSpc = new android.view.View(ctx); bSpc.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, (int)(10*dp))); fakeUiLayer.addView(bSpc);

    final android.widget.FrameLayout pinDialogLayer = new android.widget.FrameLayout(ctx); pinDialogLayer.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1)); pinDialogLayer.setBackgroundColor(0xDD000000); pinDialogLayer.setVisibility(android.view.View.GONE); pinDialogLayer.setClickable(true);
    final android.widget.LinearLayout pinCard = new android.widget.LinearLayout(ctx); pinCard.setOrientation(android.widget.LinearLayout.VERTICAL); pinCard.setLayoutParams(dCardLp); pinCard.setPadding((int)(25*dp), (int)(30*dp), (int)(25*dp), (int)(30*dp));
    final android.widget.TextView pinTitle = new android.widget.TextView(ctx); pinTitle.setTextSize(20f * tScale); pinTitle.setTypeface(kurdFont, android.graphics.Typeface.BOLD); pinTitle.setGravity(android.view.Gravity.CENTER); pinTitle.setPadding(0, 0, 0, (int)(20*dp)); pinCard.addView(pinTitle);
    final android.widget.EditText pinInput = new android.widget.EditText(ctx); pinInput.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); pinInput.setTextColor(0xFFFFFFFF); pinInput.setTextSize(28f * tScale); pinInput.setTypeface(kurdFont); pinInput.setGravity(android.view.Gravity.CENTER); pinInput.setPadding(0, (int)(15*dp), 0, (int)(15*dp)); pinInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD); pinInput.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(4)}); pinCard.addView(pinInput);
    final android.widget.LinearLayout pinBtnRow = new android.widget.LinearLayout(ctx); pinBtnRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); pinBtnRow.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); pinBtnRow.setPadding(0, (int)(25*dp), 0, 0);
    final android.widget.TextView pinCancelBtn = new android.widget.TextView(ctx); pinCancelBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f)); pinCancelBtn.setTextSize(14f * tScale); pinCancelBtn.setTextColor(0xFFFFFFFF); pinCancelBtn.setTypeface(kurdFont); pinCancelBtn.setGravity(android.view.Gravity.CENTER); pinCancelBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); pinBtnRow.addView(pinCancelBtn);
    final android.widget.TextView pinConfirmBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams pcbLp = new android.widget.LinearLayout.LayoutParams(0, -2, 1f); pcbLp.setMargins((int)(15*dp), 0, 0, 0); pinConfirmBtn.setLayoutParams(pcbLp); pinConfirmBtn.setTextSize(14f * tScale); pinConfirmBtn.setTypeface(kurdFont, android.graphics.Typeface.BOLD); pinConfirmBtn.setGravity(android.view.Gravity.CENTER); pinConfirmBtn.setPadding(0, (int)(12*dp), 0, (int)(12*dp)); pinBtnRow.addView(pinConfirmBtn);
    pinCard.addView(pinBtnRow); pinDialogLayer.addView(pinCard);
    uiUpdaters.add(new Runnable() {
        public void run() {
            int mColor = ui.getMainColor();
            int curDesign = vaultPrefs.getInt("pref_vault_design", 0);
            if(curDesign == 0) {
                starryNightView.setVisibility(android.view.View.GONE); paradiseSkyView.setVisibility(android.view.View.GONE);
                vaultLayer.setBackground(new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.BR_TL, new int[]{0xFF03030A, 0xFF0D0D20, 0xFF0A0818, 0xFF03030A}));
                orb1.setVisibility(android.view.View.VISIBLE); orb2.setVisibility(android.view.View.VISIBLE);
                android.graphics.drawable.GradientDrawable o1 = new android.graphics.drawable.GradientDrawable(); o1.setShape(android.graphics.drawable.GradientDrawable.OVAL); o1.setColors(new int[]{0x88000000 | (mColor & 0xFFFFFF), 0x33000000 | (mColor & 0xFFFFFF), 0x00000000}); o1.setGradientType(android.graphics.drawable.GradientDrawable.RADIAL_GRADIENT); o1.setGradientRadius(150*dp); orb1.setBackground(o1);
                android.graphics.drawable.GradientDrawable o2 = new android.graphics.drawable.GradientDrawable(); o2.setShape(android.graphics.drawable.GradientDrawable.OVAL); o2.setColors(new int[]{0x55000000 | (mColor & 0xFFFFFF), 0x18000000 | (mColor & 0xFFFFFF), 0x00000000}); o2.setGradientType(android.graphics.drawable.GradientDrawable.RADIAL_GRADIENT); o2.setGradientRadius(120*dp); orb2.setBackground(o2);
            } else if(curDesign == 1) {
                starryNightView.setVisibility(android.view.View.GONE); paradiseSkyView.setVisibility(android.view.View.GONE);
                vaultLayer.setBackground(new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFF080818, 0xFF150A28, 0xFF0E1832, 0xFF0A0A1A}));
                orb1.setVisibility(android.view.View.VISIBLE); orb2.setVisibility(android.view.View.VISIBLE); butterflyView.setVisibility(android.view.View.GONE);
                android.graphics.drawable.GradientDrawable mo1 = new android.graphics.drawable.GradientDrawable(); mo1.setShape(android.graphics.drawable.GradientDrawable.OVAL); mo1.setColors(new int[]{0x44BF5AF2, 0x18BF5AF2, 0x00000000}); mo1.setGradientType(android.graphics.drawable.GradientDrawable.RADIAL_GRADIENT); mo1.setGradientRadius(140*dp); orb1.setBackground(mo1);
                android.graphics.drawable.GradientDrawable mo2 = new android.graphics.drawable.GradientDrawable(); mo2.setShape(android.graphics.drawable.GradientDrawable.OVAL); mo2.setColors(new int[]{0x2800F0FF, 0x1000F0FF, 0x00000000}); mo2.setGradientType(android.graphics.drawable.GradientDrawable.RADIAL_GRADIENT); mo2.setGradientRadius(115*dp); orb2.setBackground(mo2);
            } else if(curDesign == 2) {
                starryNightView.setVisibility(android.view.View.VISIBLE); paradiseSkyView.setVisibility(android.view.View.GONE);
                vaultLayer.setBackground(new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xFF000003, 0xFF000A15, 0xFF001028, 0xFF000818}));
                orb1.setVisibility(android.view.View.GONE); orb2.setVisibility(android.view.View.GONE); butterflyView.setVisibility(android.view.View.GONE);
            } else if(curDesign == 3) {
                starryNightView.setVisibility(android.view.View.GONE); paradiseSkyView.setVisibility(android.view.View.VISIBLE);
                vaultLayer.setBackground(new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFFA8D0FF, 0xFFBFC0EE, 0xFFD4AAD8, 0xFFDDB4E4, 0xFFE8C8EC}));
                orb1.setVisibility(android.view.View.GONE); orb2.setVisibility(android.view.View.GONE);
                butterflyView.setVisibility(android.view.View.VISIBLE);
            }
            { int cd3 = vaultPrefs.getInt("pref_vault_design", 0); if(cd3 == 3) { android.graphics.drawable.GradientDrawable hbg3 = new android.graphics.drawable.GradientDrawable(); hbg3.setColor(0x66FFFFFF); hbg3.setCornerRadius(22f*dp); hbg3.setStroke((int)(0.6f*dp), 0x88FFFFFF); headerRow.setBackground(hbg3); if(android.os.Build.VERSION.SDK_INT>=21) headerRow.setElevation(6f*dp); } else { android.graphics.drawable.GradientDrawable hBgDark = new android.graphics.drawable.GradientDrawable(); hBgDark.setColor(0x18FFFFFF); hBgDark.setCornerRadius(18f*dp); hBgDark.setStroke((int)(0.6f*dp), 0x28FFFFFF); headerRow.setBackground(hBgDark); if(android.os.Build.VERSION.SDK_INT>=21) headerRow.setElevation(0f); } } settingsBtn.setTextColor(mColor);
            supportBtn.setTextColor(mColor); supportCard.setBackground(ui.glass(0xF0101025, mColor, 24f)); supportTitle.setTextColor(mColor); fastPayBtn.setBackground(ui.glass(0x18FFFFFF, 0x44FFFFFF, 14f)); korekBtn.setBackground(ui.glass(0x18FFFFFF, 0x44FFFFFF, 14f)); supportCloseBtn.setBackground(ui.glass(0x18000000 | (mColor & 0xFFFFFF), mColor, 14f)); supportCloseBtn.setTextColor(mColor);
            { int cdX = vaultPrefs.getInt("pref_vault_design", 0);
            if(cdX == 3) {
                android.graphics.drawable.GradientDrawable sbg3 = new android.graphics.drawable.GradientDrawable(); sbg3.setColor(0x66FFFFFF); sbg3.setCornerRadius(24f*dp); sbg3.setStroke((int)(0.6f*dp), 0x88FFFFFF); searchInput.setBackground(sbg3); searchInput.setHintTextColor(0x77445566); searchInput.setTextColor(0xFF2A2A3E);
                android.graphics.drawable.GradientDrawable fabBg = new android.graphics.drawable.GradientDrawable();
                fabBg.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                fabBg.setCornerRadius((int)(32.5f*dp));
                fabBg.setColors(new int[]{0xFF00D4FF, 0xFF00AADD});
                fabBg.setOrientation(android.graphics.drawable.GradientDrawable.Orientation.TL_BR);
                fabBg.setStroke((int)(0.5f*dp), 0x66FFFFFF);
                fabAdd.setBackground(fabBg);
                fabAdd.setTextColor(0xFFFFFFFF);
                if(android.os.Build.VERSION.SDK_INT>=21) { fabAdd.setElevation(14f*dp); }
                android.animation.ValueAnimator fabPulse = android.animation.ValueAnimator.ofFloat(1f, 1.06f, 1f);
                fabPulse.setDuration(2800); fabPulse.setRepeatCount(android.animation.ValueAnimator.INFINITE);
                fabPulse.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
                fabPulse.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(android.animation.ValueAnimator va) {
                        float s = (float)va.getAnimatedValue(); fabAdd.setScaleX(s); fabAdd.setScaleY(s);
                    }
                }); fabPulse.start();
            } else {
                searchInput.setBackground(ui.glass(0x15FFFFFF, 0x28FFFFFF, 18f)); searchInput.setHintTextColor(0x66FFFFFF); searchInput.setTextColor(0xFFFFFFFF);
                android.graphics.drawable.GradientDrawable fBg2 = new android.graphics.drawable.GradientDrawable(); fBg2.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE); fBg2.setCornerRadius((int)(32.5f*dp)); fBg2.setColor(0xFF000000 | (mColor & 0xFFFFFF)); fBg2.setStroke((int)(0.5f*dp), 0x44FFFFFF); fabAdd.setBackground(fBg2); fabAdd.setTextColor(0xFFFFFFFF);
                if(android.os.Build.VERSION.SDK_INT>=21) { fabAdd.setElevation(12f*dp); }
            } }
            editCard.setBackground(ui.glass(0xF0101025, mColor, 24f)); inputTitle.setBackground(ui.glass(0x33000000, 0x33FFFFFF, 14f)); inputContent.setBackground(ui.glass(0x33000000, 0x33FFFFFF, 14f)); cancelEditBtn.setBackground(ui.glass(0x18FFFFFF, 0x44FFFFFF, 14f)); saveEditBtn.setBackground(ui.glass(0x18000000 | (mColor & 0xFFFFFF), mColor, 14f)); saveEditBtn.setTextColor(mColor);
            setCard.setBackground(ui.glass(0xF0101025, mColor, 24f)); setTitle.setTextColor(mColor); setCloseBtn.setBackground(ui.glass(0x18FFFFFF, 0x44FFFFFF, 14f)); tutRedoBtn.setTextColor(mColor); tutRedoBtn.setBackground(ui.glass(0x0D000000 | (mColor & 0xFFFFFF), mColor, 14f));
            delCard.setBackground(ui.glass(0xF0101025, 0xFFFF4444, 24f)); delNoBtn.setBackground(ui.glass(0x18FFFFFF, 0x44FFFFFF, 14f)); delYesBtn.setBackground(ui.glass(0x22FF4444, 0x88FF4444, 14f));
            pinCard.setBackground(ui.glass(0xF0101025, mColor, 24f)); pinTitle.setTextColor(mColor); pinInput.setBackground(ui.glass(0x44000000, 0x33FFFFFF, 14f)); pinCancelBtn.setBackground(ui.glass(0x18FFFFFF, 0x44FFFFFF, 14f)); pinConfirmBtn.setBackground(ui.glass(0x18000000 | (mColor & 0xFFFFFF), mColor, 14f)); pinConfirmBtn.setTextColor(mColor);
            infoCard.setBackground(ui.glass(0xF2060618, mColor, 24f)); infoTitle.setTextColor(mColor); infoOkBtn.setTextColor(mColor); infoOkBtn.setBackground(ui.glass(0x15000000, mColor, 16f));
            tutCard.setBackground(ui.glass(0xF0101025, mColor, 24f)); tutTitle.setTextColor(mColor); tutNextBtn.setTextColor(mColor); tutNextBtn.setBackground(ui.glass(0x18000000 | (mColor & 0xFFFFFF), mColor, 14f));
            hint9Card.setBackground(ui.glass(0xF0101025, mColor, 18f));
            setIntroCard.setBackground(ui.glass(0xF0101025, mColor, 24f)); setIntroTitle.setTextColor(mColor); setIntroBtn.setTextColor(mColor); setIntroBtn.setBackground(ui.glass(0x18000000 | (mColor & 0xFFFFFF), mColor, 14f));
            scrollHint.setBackground(ui.glass(0x77000000, 0xFFFFD700, 18f));
            readModeTitle.setTextColor(mColor); readModeCloseBtn.setBackground(ui.glass(0x22000000, mColor, 18f)); readModeCloseBtn.setTextColor(mColor); intruderCloseBtn.setBackground(ui.glass(0x22000000, mColor, 18f)); intruderCloseBtn.setTextColor(mColor);
            if((curDesign == 1 || curDesign == 2 || curDesign == 3) && refreshDataListHolder[0] != null) refreshDataListHolder[0].run();
            updateProAudio.run();
        }
    });
    uiUpdaters.add(new Runnable() {
        public void run() {
            vaultTitle.setText("🛡️ " + tr.get("vault_title")); searchInput.setHint(tr.get("search_hint")); inputTitle.setHint(tr.get("hint_title")); inputContent.setHint(tr.get("hint_content")); saveEditBtn.setText(tr.get("save")); cancelEditBtn.setText(tr.get("cancel")); setTitle.setText("⚙️ " + tr.get("set_title")); tutRedoBtn.setText(tr.get("tut_redo"));
            lblShake[0].setText(tr.get("shake")); subShake[0].setText(tr.get("sub_shake")); lblSecure[0].setText(tr.get("secure_screen")); subSecure[0].setText(tr.get("sub_secure")); lblColor[0].setText("🎨 " + tr.get("main_color")); subColor[0].setText(tr.get("sub_color")); lblStyle[0].setText("📐 " + tr.get("card_style")); subStyle[0].setText(tr.get("sub_style"));
            btnGlass.setText(tr.get("style_glass")); btnMin.setText(tr.get("style_min")); lblLock[0].setText("⏱️ " + tr.get("auto_lock")); subLock[0].setText(tr.get("sub_lock"));
            lkBtns[0].setText(tr.get("al_30s")); lkBtns[1].setText(tr.get("al_1m")); lkBtns[2].setText(tr.get("al_5m")); lkBtns[3].setText(tr.get("al_never"));
            lblExp.setText(tr.get("export_data")); subExp.setText(tr.get("sub_exp")); lblImp.setText(tr.get("import_data")); subImp.setText(tr.get("sub_imp")); lblDelAll.setText(tr.get("delete_all")); setCloseBtn.setText(tr.get("back")); delNoBtn.setText(tr.get("del_no")); delYesBtn.setText(tr.get("del_yes")); pinCancelBtn.setText(tr.get("cancel")); pinConfirmBtn.setText(tr.get("confirm"));
            catBtns[0].setText(tr.get("cat_normal")); catBtns[1].setText(tr.get("cat_important")); catBtns[2].setText(tr.get("cat_work")); catBtns[3].setText(tr.get("cat_personal"));
            setIntroTitle.setText(tr.get("set_intro_title")); setIntroMsg.setText(tr.get("tooltip_set")); setIntroBtn.setText(tr.get("info_ok"));
            hint9TxtKu.setText(tr.get("hint_9_toast")); hint9TxtEn.setText(tr.get("hint_9_sub")); scrollHint.setText(tr.get("scroll_hint"));
            lblDt[0].setText(tr.get("dt_title")); subDt[0].setText(tr.get("dt_sub")); dtTimeLabel.setText(tr.get("dt_time_label")); dtActWipe.setText(tr.get("dt_action_wipe")); dtWarningNote.setText(tr.get("dt_warning_note"));
            vaultDesignHeader.setText(tr.get("vault_design_title")); vdLbl.setText(tr.get("vault_design_title")); vdSub.setText(tr.get("vault_design_sub"));
            btnDesignClassic.setText(tr.get("vault_design_classic")); btnDesignModern.setText(tr.get("vault_design_modern"));
            proDesignHeader.setText(tr.get("vault_design_pro_title")); pdLbl.setText(tr.get("vault_design_pro_title")); pdSub.setText(tr.get("pro_themes_sub"));
            btnDesignFirefly.setText(tr.get("vault_design_firefly")); btnDesignParadise.setText(tr.get("vault_design_paradise"));
            readModeCloseBtn.setText(tr.get("read_mode_close"));
            intruderToggleLbl.setText(tr.get("intruder_log_btn")); intruderToggleSub.setText(tr.get("intruder_sub")); intruderLogBtn.setText("🕵️ " + tr.get("intruder_log_btn")); intruderCloseBtn.setText(tr.get("back"));
            if(refreshDataListHolder[0] != null) refreshDataListHolder[0].run(); updateTutUI.run();
        }
    });
    refreshDataListHolder[0] = new Runnable() {
        @Override public void run() {
            dataListContainer.removeAllViews(); String query = searchInput.getText().toString().trim().toLowerCase();
            int curDesign = vaultPrefs.getInt("pref_vault_design", 0);
            try {
                org.json.JSONArray jsonArray = new org.json.JSONArray(vaultPrefs.getString("secrets_array", "[]"));
                if (jsonArray.length() == 0) { android.widget.TextView emptyTxt = new android.widget.TextView(ctx); emptyTxt.setTextColor(0x88FFFFFF); emptyTxt.setTypeface(kurdFont); emptyTxt.setGravity(android.view.Gravity.CENTER); emptyTxt.setText(tr.get("empty")); emptyTxt.setPadding(0,(int)(50*dp),0,0); dataListContainer.addView(emptyTxt); } else {
                    java.util.ArrayList<org.json.JSONObject> pinned = new java.util.ArrayList<>();
                    java.util.ArrayList<org.json.JSONObject> unpinned = new java.util.ArrayList<>();
                    for(int i=0; i<jsonArray.length(); i++) {
                        org.json.JSONObject o2 = jsonArray.optJSONObject(i);
                        if(o2==null) continue;
                        if(o2.optBoolean("pin",false)) pinned.add(0,o2); else unpinned.add(0,o2);
                    }
                    java.util.ArrayList<org.json.JSONObject> ordered = new java.util.ArrayList<>();
                    ordered.addAll(pinned); ordered.addAll(unpinned);
                    for (int i = 0; i < ordered.size(); i++) {
                        final org.json.JSONObject obj = ordered.get(i);

                        final String nId = obj.optString("id", ""); final String nTitle = obj.optString("title", ""); final String nContent = obj.optString("content", ""); final long nTime = obj.optLong("time", 0); final int nCat = obj.optInt("cat", 0); final boolean nFav = obj.optBoolean("fav", false); final boolean nPin = obj.optBoolean("pin", false);
                        if (!query.isEmpty() && !nTitle.toLowerCase().contains(query) && !nContent.toLowerCase().contains(query)) continue;
                        final android.widget.LinearLayout itemCard = new android.widget.LinearLayout(ctx); itemCard.setOrientation(android.widget.LinearLayout.VERTICAL); android.widget.LinearLayout.LayoutParams icLp = new android.widget.LinearLayout.LayoutParams(-1, -2); icLp.setMargins(0, 0, 0, (int)(15*dp*sScale)); itemCard.setLayoutParams(icLp); itemCard.setPadding((int)(15*dp), (int)(15*dp), (int)(15*dp), (int)(15*dp));
                        if(curDesign == 3) {
                            final android.widget.TextView bfly = new android.widget.TextView(ctx);
                            bfly.setText("🦋"); bfly.setTextSize(18f * tScale);
                            android.widget.LinearLayout.LayoutParams bfLp = new android.widget.LinearLayout.LayoutParams(-2, -2);
                            bfly.setLayoutParams(bfLp);
                            itemCard.addView(bfly);
                            android.animation.ObjectAnimator bfScX = android.animation.ObjectAnimator.ofFloat(bfly, "scaleX", 1f, 0.15f, 1f);
                            bfScX.setDuration(320); bfScX.setRepeatCount(android.animation.ValueAnimator.INFINITE); bfScX.setRepeatMode(android.animation.ValueAnimator.REVERSE); bfScX.start();
                            android.animation.ObjectAnimator bfTx = android.animation.ObjectAnimator.ofFloat(bfly, "translationX", 0f, 15f, -5f, 10f, 0f);
                            bfTx.setDuration(2800); bfTx.setRepeatCount(android.animation.ValueAnimator.INFINITE); bfTx.setRepeatMode(android.animation.ValueAnimator.REVERSE); bfTx.start();
                        }
                        int cColor = catColors[nCat % 4];
                        if(curDesign == 0) {
                            android.graphics.drawable.GradientDrawable classicCard = new android.graphics.drawable.GradientDrawable();
                            classicCard.setCornerRadius(18f * dp); classicCard.setColor(0x1A000000 | (cColor & 0xFFFFFF)); classicCard.setStroke((int)(0.8f * dp), 0x55000000 | (cColor & 0xFFFFFF));
                            itemCard.setBackground(classicCard);
                            if(android.os.Build.VERSION.SDK_INT >= 21) itemCard.setElevation(3f*dp);
                        }
                        else if(curDesign == 1) { android.graphics.drawable.GradientDrawable modernCard = new android.graphics.drawable.GradientDrawable(); modernCard.setCornerRadius(22f * dp); modernCard.setColor(0x22000000 | (cColor & 0xFFFFFF)); modernCard.setStroke((int)(1f * dp), 0x66000000 | (cColor & 0xFFFFFF)); itemCard.setBackground(modernCard); if(android.os.Build.VERSION.SDK_INT>=21) itemCard.setElevation(4f*dp); }
                        else if(curDesign == 2) { android.graphics.drawable.GradientDrawable fireflyCard = new android.graphics.drawable.GradientDrawable(); fireflyCard.setCornerRadius(16f * dp); fireflyCard.setColor(0x33000008 | (cColor & 0xFFFFFF)); fireflyCard.setStroke((int)(0.8f * dp), 0x88DDFF00); itemCard.setBackground(fireflyCard); }
                        else if(curDesign == 3) {
    android.graphics.drawable.GradientDrawable paradiseCard = new android.graphics.drawable.GradientDrawable();
    paradiseCard.setCornerRadius(26f * dp); paradiseCard.setColor(0x66FFFFFF); paradiseCard.setStroke((int)(0.5f * dp), 0x55FFFFFF);
    itemCard.setBackground(paradiseCard);
    if(android.os.Build.VERSION.SDK_INT >= 21) { itemCard.setElevation(10f * dp); }
    itemCard.setPadding((int)(20*dp), (int)(20*dp), (int)(20*dp), (int)(20*dp));
    }
                        final int cardIndex = i;
                        itemCard.setAlpha(0f); itemCard.setTranslationY(40f); itemCard.setScaleX(0.96f); itemCard.setScaleY(0.96f);
                        itemCard.animate().alpha(1f).translationY(0f).scaleX(1f).scaleY(1f).setDuration(450).setStartDelay(Math.max(0, cardIndex * 50)).setInterpolator(new android.view.animation.OvershootInterpolator(0.8f)).start();
                        android.widget.LinearLayout tRow = new android.widget.LinearLayout(ctx); tRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); tRow.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2));
                        android.widget.TextView tTxt = new android.widget.TextView(ctx); tTxt.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f)); tTxt.setText(nTitle); tTxt.setTextSize(18f * tScale); tTxt.setTextColor(curDesign == 3 ? 0xFF1A1A2E : 0xFFFFFFFF); tTxt.setTypeface(kurdFont, android.graphics.Typeface.BOLD); tTxt.setSingleLine(true); tTxt.setEllipsize(android.text.TextUtils.TruncateAt.END);
                        final android.widget.TextView fBtn = new android.widget.TextView(ctx); fBtn.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2)); fBtn.setText(nFav ? "⭐" : "☆"); fBtn.setTextSize(18f * tScale); fBtn.setPadding((int)(10*dp), 0, 0, 0);
                        if(nPin) { android.widget.TextView pinBadge = new android.widget.TextView(ctx); pinBadge.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2,-2)); pinBadge.setText("📌"); pinBadge.setTextSize(13f * tScale); pinBadge.setPadding((int)(4*dp),0,0,0); tRow.addView(tTxt); tRow.addView(pinBadge); tRow.addView(fBtn); } else { tRow.addView(tTxt); tRow.addView(fBtn); }
                        itemCard.addView(tRow);
                        if(nPin) { android.graphics.drawable.GradientDrawable pinBg = new android.graphics.drawable.GradientDrawable(); pinBg.setColor(0x11FF9900); pinBg.setCornerRadius(20f*dp); pinBg.setStroke((int)(1.5f*dp), 0xAAFF9900); itemCard.setBackground(pinBg); }
                        android.widget.TextView cTxt = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams cLpx = new android.widget.LinearLayout.LayoutParams(-1, -2); cLpx.setMargins(0, (int)(8*dp), 0, (int)(12*dp)); cTxt.setLayoutParams(cLpx); cTxt.setText(nContent); cTxt.setTextSize(14f * tScale); cTxt.setTextColor(curDesign == 3 ? 0xFF3A3A4E : 0xFFBBBBBB); cTxt.setTypeface(kurdFont); cTxt.setMaxLines(2); cTxt.setEllipsize(android.text.TextUtils.TruncateAt.END); cTxt.setLineSpacing(2f, 1.2f); itemCard.addView(cTxt);
                        android.widget.LinearLayout bRow = new android.widget.LinearLayout(ctx); bRow.setOrientation(android.widget.LinearLayout.HORIZONTAL); bRow.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -2)); bRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
                        android.widget.TextView tmTxt = new android.widget.TextView(ctx); tmTxt.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, -2, 1f)); tmTxt.setText(tools.formatTime(nTime)); tmTxt.setTextSize(11f * tScale); tmTxt.setTextColor(curDesign == 3 ? 0xFF5A5A6E : 0xFF888888); tmTxt.setTypeface(kurdFont);
                        android.widget.TextView eBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams eBLp = new android.widget.LinearLayout.LayoutParams(-2,-2); eBLp.setMargins((int)(6*dp),0,0,0); eBtn.setLayoutParams(eBLp); eBtn.setText("✏️"); eBtn.setTextSize(15f * tScale); eBtn.setPadding((int)(8*dp),(int)(6*dp),(int)(8*dp),(int)(6*dp));
                        android.widget.TextView rdBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams rBLp = new android.widget.LinearLayout.LayoutParams(-2,-2); rBLp.setMargins((int)(6*dp),0,0,0); rdBtn.setLayoutParams(rBLp); rdBtn.setText("📖"); rdBtn.setTextSize(15f * tScale); rdBtn.setPadding((int)(8*dp),(int)(6*dp),(int)(8*dp),(int)(6*dp));
                        android.widget.TextView cpBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams cBLp = new android.widget.LinearLayout.LayoutParams(-2,-2); cBLp.setMargins((int)(6*dp),0,0,0); cpBtn.setLayoutParams(cBLp); cpBtn.setText("📋"); cpBtn.setTextSize(15f * tScale); cpBtn.setPadding((int)(8*dp),(int)(6*dp),(int)(8*dp),(int)(6*dp));
                        android.widget.TextView dBtn = new android.widget.TextView(ctx); android.widget.LinearLayout.LayoutParams dBLp = new android.widget.LinearLayout.LayoutParams(-2,-2); dBLp.setMargins((int)(6*dp),0,0,0); dBtn.setLayoutParams(dBLp); dBtn.setText("🗑️"); dBtn.setTextSize(15f * tScale); dBtn.setPadding((int)(8*dp),(int)(6*dp),(int)(8*dp),(int)(6*dp));
                        android.widget.LinearLayout iconPill = new android.widget.LinearLayout(ctx);
                        iconPill.setOrientation(android.widget.LinearLayout.HORIZONTAL);
                        iconPill.setGravity(android.view.Gravity.CENTER);
                        android.widget.LinearLayout.LayoutParams pillLp = new android.widget.LinearLayout.LayoutParams(-2, -2);
                        iconPill.setLayoutParams(pillLp);
                        iconPill.setPadding((int)(10*dp),(int)(6*dp),(int)(10*dp),(int)(6*dp));
                        android.graphics.drawable.GradientDrawable pillBg = new android.graphics.drawable.GradientDrawable();
                        pillBg.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
                        pillBg.setCornerRadius(50f * dp);
                        pillBg.setColor(curDesign == 3 ? 0x22000000 : 0x22FFFFFF);
                        pillBg.setStroke((int)(0.5f*dp), curDesign == 3 ? 0x22000000 : 0x33FFFFFF);
                        iconPill.setBackground(pillBg);
                        if(android.os.Build.VERSION.SDK_INT >= 21) iconPill.setElevation(curDesign == 3 ? 4f*dp : 2f*dp);
                        iconPill.addView(rdBtn); iconPill.addView(eBtn); iconPill.addView(cpBtn); iconPill.addView(dBtn);
                        bRow.addView(tmTxt); bRow.addView(iconPill);
                        itemCard.addView(bRow);
                        dataListContainer.addView(itemCard);
                        if(curDesign != 3) {
                            final int ci2 = i;
                            itemCard.setAlpha(0f); itemCard.setTranslationY(35f); itemCard.setScaleX(0.97f); itemCard.setScaleY(0.97f);
                            itemCard.animate().alpha(1f).translationY(0f).scaleX(1f).scaleY(1f).setDuration(420).setStartDelay(Math.max(0, ci2 * 45)).setInterpolator(new android.view.animation.OvershootInterpolator(0.6f)).start();
                        }
                        final String finalNTitle = nTitle; final String finalNContent = nContent;
                        rdBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); readModeTitle.setText(finalNTitle); readModeContent.setText(finalNContent); readModeScroll.scrollTo(0, 0); readModeLayer.setVisibility(android.view.View.VISIBLE); readModeLayer.setAlpha(0f); try{ android.animation.ObjectAnimator.ofFloat(readModeLayer, "alpha", 0f, 1f).setDuration(300).start(); }catch(Exception e){} } });
                        itemCard.setOnLongClickListener(new android.view.View.OnLongClickListener() { public boolean onLongClick(android.view.View v) { tools.vibe(60); try { org.json.JSONArray arr = new org.json.JSONArray(vaultPrefs.getString("secrets_array","[]")); for(int k=0;k<arr.length();k++){org.json.JSONObject o=arr.getJSONObject(k); if(o.optString("id").equals(nId)){o.put("pin",!nPin);break;}} vaultPrefs.edit().putString("secrets_array",arr.toString()).apply(); android.widget.Toast.makeText(ctx, nPin?"📌 پین لادرا":"📌 پین کرا", 0).show(); if(refreshDataListHolder[0]!=null) refreshDataListHolder[0].run(); } catch(Exception ex){} return true; } });
                        fBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); try { org.json.JSONArray arr = new org.json.JSONArray(vaultPrefs.getString("secrets_array", "[]")); for(int k=0; k<arr.length(); k++) { org.json.JSONObject o = arr.getJSONObject(k); if(o.optString("id").equals(nId)) { o.put("fav", !nFav); break; } } vaultPrefs.edit().putString("secrets_array", arr.toString()).apply(); if(refreshDataListHolder[0]!=null) refreshDataListHolder[0].run(); } catch(Exception ex){} } });
                        cpBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); try { android.content.ClipboardManager cb = (android.content.ClipboardManager) ctx.getSystemService(android.content.Context.CLIPBOARD_SERVICE); if(cb!=null) { cb.setPrimaryClip(android.content.ClipData.newPlainText("Vault", nContent)); android.widget.Toast.makeText(ctx, tr.get("copied"), 0).show(); } } catch(Exception e){} } });
                        eBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); editingId[0] = nId; inputTitle.setText(nTitle); inputContent.setText(nContent); catBtns[nCat].performClick(); editLayer.setVisibility(android.view.View.VISIBLE); editLayer.setAlpha(0f); editCard.setScaleY(0.8f); try{ android.animation.ObjectAnimator.ofFloat(editLayer, "alpha", 0f, 1f).setDuration(300).start(); android.animation.ObjectAnimator.ofFloat(editCard, "scaleY", 0.8f, 1f).setDuration(300).start(); }catch(Exception e){} } });
                        dBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); delTitle.setText(tr.get("del_title")); delMsg.setText(tr.get("del_msg")); delYesBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View vy) { ui.bounce(vy); tools.vibe(80); delLayer.setVisibility(android.view.View.GONE);
    android.animation.AnimatorSet del = new android.animation.AnimatorSet();
    android.animation.ObjectAnimator delAlpha = android.animation.ObjectAnimator.ofFloat(itemCard, "alpha", 1f, 0f);
    android.animation.ObjectAnimator delScaleX = android.animation.ObjectAnimator.ofFloat(itemCard, "scaleX", 1f, 0.88f);
    android.animation.ObjectAnimator delScaleY = android.animation.ObjectAnimator.ofFloat(itemCard, "scaleY", 1f, 0.85f);
    android.animation.ObjectAnimator delTy = android.animation.ObjectAnimator.ofFloat(itemCard, "translationY", 0f, 25f);
    android.animation.ObjectAnimator delTx = android.animation.ObjectAnimator.ofFloat(itemCard, "translationX", 0f, -60f);
    delAlpha.setDuration(420); delScaleX.setDuration(420); delScaleY.setDuration(420); delTy.setDuration(420); delTx.setDuration(420);
    del.playTogether(delAlpha, delScaleX, delScaleY, delTy, delTx);
    del.setInterpolator(new android.view.animation.AccelerateInterpolator(1.8f));
    del.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) {
        try { org.json.JSONArray arr = new org.json.JSONArray(vaultPrefs.getString("secrets_array","[]")); org.json.JSONArray newArr = new org.json.JSONArray(); for(int k=0;k<arr.length();k++){if(!arr.getJSONObject(k).optString("id").equals(nId)) newArr.put(arr.getJSONObject(k));} vaultPrefs.edit().putString("secrets_array",newArr.toString()).apply(); if(refreshDataListHolder[0]!=null) refreshDataListHolder[0].run(); } catch(Exception ex){}
    } });
    del.start();
} }); delNoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View vn) { ui.bounce(vn); delLayer.setVisibility(android.view.View.GONE); } }); delLayer.setVisibility(android.view.View.VISIBLE); delLayer.setAlpha(0f); delCard.setScaleY(0.8f); try{ android.animation.ObjectAnimator.ofFloat(delLayer, "alpha", 0f, 1f).setDuration(200).start(); android.animation.ObjectAnimator.ofFloat(delCard, "scaleY", 0.8f, 1f).setDuration(200).start(); }catch(Exception e){} } });
                    }
                }
            } catch (Exception e) {}
        }
    };
    searchInput.addTextChangedListener(new android.text.TextWatcher() { @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {} @Override public void onTextChanged(CharSequence s, int start, int before, int count) { if (refreshDataListHolder[0] != null) refreshDataListHolder[0].run(); } @Override public void afterTextChanged(android.text.Editable s) {} });
    fabAdd.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); editingId[0] = ""; inputTitle.setText(""); inputContent.setText(""); catBtns[0].performClick(); editLayer.setVisibility(android.view.View.VISIBLE); editLayer.setAlpha(0f); editCard.setScaleX(0.9f); editCard.setScaleY(0.9f); editCard.setTranslationY(40f); try{ android.animation.ObjectAnimator.ofFloat(editLayer, "alpha", 0f, 1f).setDuration(300).start(); android.animation.ObjectAnimator sx = android.animation.ObjectAnimator.ofFloat(editCard, "scaleX", 0.9f, 1f); sx.setDuration(400); sx.setInterpolator(new android.view.animation.OvershootInterpolator(1.1f)); sx.start(); android.animation.ObjectAnimator sy = android.animation.ObjectAnimator.ofFloat(editCard, "scaleY", 0.9f, 1f); sy.setDuration(400); sy.setInterpolator(new android.view.animation.OvershootInterpolator(1.1f)); sy.start(); android.animation.ObjectAnimator.ofFloat(editCard, "translationY", 40f, 0f).setDuration(350).start(); }catch(Exception e){} inputTitle.requestFocus(); tools.showKeyboard(inputTitle); } });
    cancelEditBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(30); tools.hideKeyboard(inputTitle); try{ android.animation.ObjectAnimator fade = android.animation.ObjectAnimator.ofFloat(editLayer, "alpha", 1f, 0f); fade.setDuration(250); fade.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { editLayer.setVisibility(android.view.View.GONE); }}); fade.start(); }catch(Exception e){} } });
    saveEditBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); String t = inputTitle.getText().toString().trim(); String c = inputContent.getText().toString().trim(); if(c.isEmpty()) { inputContent.setHintTextColor(0xFFFF4444); return; } try { org.json.JSONArray arr = new org.json.JSONArray(vaultPrefs.getString("secrets_array", "[]")); if(editingId[0].isEmpty()) { org.json.JSONObject o = new org.json.JSONObject(); o.put("id", java.util.UUID.randomUUID().toString()); o.put("title", t.isEmpty() ? "نهێنی بێ ناو" : t); o.put("content", c); o.put("time", System.currentTimeMillis()); o.put("cat", selectedCat[0]); o.put("fav", false); arr.put(o); } else { for(int i=0; i<arr.length(); i++) { org.json.JSONObject o = arr.getJSONObject(i); if(o.optString("id").equals(editingId[0])) { o.put("title", t.isEmpty() ? "نهێنی بێ ناو" : t); o.put("content", c); o.put("time", System.currentTimeMillis()); o.put("cat", selectedCat[0]); break; } } } vaultPrefs.edit().putString("secrets_array", arr.toString()).apply(); if(refreshDataListHolder[0]!=null) refreshDataListHolder[0].run(); cancelEditBtn.performClick(); android.widget.Toast.makeText(ctx, tr.get("saved_ok"), 0).show(); } catch(Exception e){} } });
    settingsBtn.setOnClickListener(new android.view.View.OnClickListener() {
        public void onClick(android.view.View v) {
            ui.bounce(v); tools.vibe(40); settingsLayer.setVisibility(android.view.View.VISIBLE); settingsLayer.setAlpha(0f); setCard.setScaleX(0.92f); setCard.setScaleY(0.92f); setCard.setTranslationY(30f);
            try{ android.animation.ObjectAnimator.ofFloat(settingsLayer, "alpha", 0f, 1f).setDuration(320).start(); android.animation.ObjectAnimator sx2 = android.animation.ObjectAnimator.ofFloat(setCard, "scaleX", 0.92f, 1f); sx2.setDuration(400); sx2.setInterpolator(new android.view.animation.OvershootInterpolator(0.8f)); sx2.start(); android.animation.ObjectAnimator sy2 = android.animation.ObjectAnimator.ofFloat(setCard, "scaleY", 0.92f, 1f); sy2.setDuration(400); sy2.setInterpolator(new android.view.animation.OvershootInterpolator(0.8f)); sy2.start(); android.animation.ObjectAnimator.ofFloat(setCard, "translationY", 30f, 0f).setDuration(350).start(); }catch(Exception e){}
            if(!vaultPrefs.getBoolean("seen_set_intro", false)) { vaultPrefs.edit().putBoolean("seen_set_intro", true).apply(); setIntroLayer.setVisibility(android.view.View.VISIBLE); setIntroLayer.setAlpha(0f); setIntroCard.setScaleY(0.8f); try{ android.animation.ObjectAnimator introA1 = android.animation.ObjectAnimator.ofFloat(setIntroLayer, "alpha", 0f, 1f); introA1.setDuration(300); introA1.setStartDelay(200); introA1.start(); android.animation.ObjectAnimator introA2 = android.animation.ObjectAnimator.ofFloat(setIntroCard, "scaleY", 0.8f, 1f); introA2.setDuration(300); introA2.setStartDelay(200); introA2.start(); }catch(Exception e){} }
            if(!vaultPrefs.getBoolean("hint_scroll_seen", false)) { scrollHint.setVisibility(android.view.View.VISIBLE); scrollHint.setAlpha(0f); try{ android.animation.ObjectAnimator a1 = android.animation.ObjectAnimator.ofFloat(scrollHint, "alpha", 0f, 1f); a1.setDuration(400); android.animation.ObjectAnimator a2 = android.animation.ObjectAnimator.ofFloat(scrollHint, "translationY", -15f, 15f); a2.setDuration(800); a2.setRepeatCount(android.animation.ValueAnimator.INFINITE); a2.setRepeatMode(android.animation.ValueAnimator.REVERSE); a1.start(); a2.start(); }catch(Exception e){} }
        }
    });
    setCloseBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); try{ android.animation.ObjectAnimator fadeOut = android.animation.ObjectAnimator.ofFloat(settingsLayer, "alpha", 1f, 0f); fadeOut.setDuration(300); fadeOut.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { settingsLayer.setVisibility(android.view.View.GONE); }}); fadeOut.start(); }catch(Exception e){} }});
    tutRedoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); vaultPrefs.edit().putBoolean("tut_calc_seen", false).putBoolean("tut_vault_seen", false).apply(); setCloseBtn.performClick(); vaultLayer.setVisibility(android.view.View.GONE); fakeUiLayer.setVisibility(android.view.View.VISIBLE); inVaultTut[0] = false; tutStep[0] = 0; updateTutUI.run(); tutLayer.setVisibility(android.view.View.VISIBLE); tutLayer.setAlpha(0f); try{ android.animation.ObjectAnimator.ofFloat(tutLayer, "alpha", 0f, 1f).setDuration(300).start(); }catch(Exception e){} } });
    lblExp.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); try { String data = vaultPrefs.getString("secrets_array", "[]"); if (data.equals("[]")) { android.widget.Toast.makeText(ctx, tr.get("empty"), 1).show(); return; } org.json.JSONArray originalArray = new org.json.JSONArray(data); org.json.JSONArray smartArray = new org.json.JSONArray(); for(int i=0; i<originalArray.length(); i++) { org.json.JSONObject obj = originalArray.getJSONObject(i); org.json.JSONArray item = new org.json.JSONArray(); item.put(obj.optString("title", "")); item.put(obj.optString("content", "")); item.put(obj.optLong("time", 0)); item.put(obj.optInt("cat", 0)); item.put(obj.optBoolean("fav", false)); smartArray.put(item); } String compressedAndEncrypted = crypto.compressAndEncrypt(smartArray.toString()); if(compressedAndEncrypted != null) { android.content.ClipboardManager cb = (android.content.ClipboardManager) ctx.getSystemService(android.content.Context.CLIPBOARD_SERVICE); if(cb != null) { cb.setPrimaryClip(android.content.ClipData.newPlainText("VaultBackup", compressedAndEncrypted)); android.widget.Toast.makeText(ctx, tr.get("exp_ok"), 1).show(); } } } catch(Exception e){} } });
    lblImp.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); try { android.content.ClipboardManager cb = (android.content.ClipboardManager) ctx.getSystemService(android.content.Context.CLIPBOARD_SERVICE); if(cb != null && cb.hasPrimaryClip() && cb.getPrimaryClip().getItemCount() > 0) { String clipboardText = cb.getPrimaryClip().getItemAt(0).getText().toString().trim(); String decryptedJson = clipboardText; if (!clipboardText.startsWith("[")) { String temp = crypto.decryptAndDecompress(clipboardText); if(temp != null && temp.startsWith("[")) decryptedJson = temp; } org.json.JSONArray importedArr = new org.json.JSONArray(decryptedJson); org.json.JSONArray finalArr = new org.json.JSONArray(); for(int i=0; i<importedArr.length(); i++) { Object item = importedArr.get(i); if (item instanceof org.json.JSONArray) { org.json.JSONArray arrItem = (org.json.JSONArray) item; org.json.JSONObject newObj = new org.json.JSONObject(); newObj.put("id", java.util.UUID.randomUUID().toString()); newObj.put("title", arrItem.optString(0, "")); newObj.put("content", arrItem.optString(1, "")); newObj.put("time", arrItem.optLong(2, 0)); newObj.put("cat", arrItem.optInt(3, 0)); newObj.put("fav", arrItem.optBoolean(4, false)); finalArr.put(newObj); } else if (item instanceof org.json.JSONObject) { org.json.JSONObject oldObj = (org.json.JSONObject) item; if (clipboardText.startsWith("[")) { String c = oldObj.optString("content", ""); oldObj.put("content", crypto.decryptLegacy(c)); } finalArr.put(oldObj); } } vaultPrefs.edit().putString("secrets_array", finalArr.toString()).apply(); if(refreshDataListHolder[0] != null) refreshDataListHolder[0].run(); android.widget.Toast.makeText(ctx, tr.get("imp_ok"), 1).show(); } } catch(Exception e){ android.widget.Toast.makeText(ctx, tr.get("imp_err"), 1).show(); } } });
    lblDelAll.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(80); delTitle.setText(tr.get("delete_all")); delMsg.setText(tr.get("del_msg")); delYesBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View vy) { vaultPrefs.edit().putString("secrets_array", "[]").apply(); if(refreshDataListHolder[0]!=null) refreshDataListHolder[0].run(); delLayer.setVisibility(android.view.View.GONE); } }); delNoBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View vn) { delLayer.setVisibility(android.view.View.GONE); } }); delLayer.setVisibility(android.view.View.VISIBLE); delLayer.setAlpha(0f); delCard.setScaleY(0.8f); try{ android.animation.ObjectAnimator.ofFloat(delLayer, "alpha", 0f, 1f).setDuration(200).start(); android.animation.ObjectAnimator.ofFloat(delCard, "scaleY", 0.8f, 1f).setDuration(200).start(); }catch(Exception e){} } });
    final Runnable showPinDialog = new Runnable() { @Override public void run() {
        pinInput.setText("");
        if(pinMode[0] == 0) { pinTitle.setText("🔐 " + tr.get("pin_new")); }
        else if(pinMode[0] == 1) { pinTitle.setText("🔒 " + tr.get("pin_enter")); }
        else if(pinMode[0] == 2) { pinTitle.setText("🔑 " + tr.get("pin_old")); }
        else if(pinMode[0] == 3) { pinTitle.setText("🆕 " + tr.get("pin_new2")); }
        else if(pinMode[0] == 5) { pinTitle.setText("🔄 " + tr.get("pin_confirm_new")); }
        pinTitle.setTextColor(ui.getMainColor());
        pinDialogLayer.setVisibility(android.view.View.VISIBLE); pinDialogLayer.setAlpha(0f); pinCard.setScaleY(0.8f); try{ android.animation.ObjectAnimator.ofFloat(pinDialogLayer, "alpha", 0f, 1f).setDuration(300).start(); android.animation.ObjectAnimator.ofFloat(pinCard, "scaleY", 0.8f, 1f).setDuration(300).start(); }catch(Exception e){} pinInput.requestFocus(); tools.showKeyboard(pinInput);
    } };
    pinCancelBtn.setOnClickListener(new android.view.View.OnClickListener() { public void onClick(android.view.View v) { ui.bounce(v); tools.vibe(40); tools.hideKeyboard(pinInput); try{ android.animation.ObjectAnimator fadeOut = android.animation.ObjectAnimator.ofFloat(pinDialogLayer, "alpha", 1f, 0f); fadeOut.setDuration(300); fadeOut.addListener(new android.animation.AnimatorListenerAdapter() { @Override public void onAnimationEnd(android.animation.Animator a) { pinDialogLayer.setVisibility(android.view.View.GONE); }}); fadeOut.start(); }catch(Exception e){} }});
    pinConfirmBtn.setOnClickListener(new android.view.View.OnClickListener() {
        public void onClick(android.view.View v) {
            ui.bounce(v); tools.vibe(40); String entered = pinInput.getText().toString();
            if(entered.length() < 4) { try{ android.animation.ObjectAnimator.ofFloat(pinCard, "translationX", 0f, 15f, -15f, 15f, -15f, 0f).setDuration(400).start(); }catch(Exception e){} return; }
            String savedPin = vaultPrefs.getString("vault_pin", "");
            if(pinMode[0] == 0) {
                vaultPrefs.edit().putString("vault_pin", entered).apply(); pinCancelBtn.performClick(); lastInteraction[0] = System.currentTimeMillis(); vaultPrefs.edit().putLong("last_open_time", System.currentTimeMillis()).apply();
                wrongPasswordCount[0] = 0;
                fakeUiLayer.setVisibility(android.view.View.GONE); vaultLayer.setVisibility(android.view.View.VISIBLE);
                updateProAudio.run();
                if(!vaultPrefs.getBoolean("tut_vault_seen", false)) { inVaultTut[0]=true; tutStep[0]=0; updateTutUI.run(); tutLayer.setVisibility(android.view.View.VISIBLE); tutLayer.setAlpha(0f); try{ android.animation.ObjectAnimator.ofFloat(tutLayer, "alpha", 0f, 1f).setDuration(400).start(); }catch(Exception e){} }
                
            }
            else if(pinMode[0] == 1) {
                if(entered.equals(savedPin)) {
                    wrongPasswordCount[0] = 0;
                    pinCancelBtn.performClick(); lastInteraction[0] = System.currentTimeMillis(); vaultPrefs.edit().putLong("last_open_time", System.currentTimeMillis()).apply();
                    fakeUiLayer.setVisibility(android.view.View.GONE); vaultLayer.setVisibility(android.view.View.VISIBLE);
                    updateProAudio.run();
                    if(!vaultPrefs.getBoolean("tut_vault_seen", false)) { inVaultTut[0]=true; tutStep[0]=0; updateTutUI.run(); tutLayer.setVisibility(android.view.View.VISIBLE); tutLayer.setAlpha(0f); try{ android.animation.ObjectAnimator.ofFloat(tutLayer, "alpha", 0f, 1f).setDuration(400).start(); }catch(Exception e){} }
                    
                } else {
                    wrongPasswordCount[0]++;
                    pinInput.setText(""); pinTitle.setText(tr.get("pin_err")); pinTitle.setTextColor(0xFFFF4444);
                    try{ android.animation.ObjectAnimator.ofFloat(pinCard, "translationX", 0f, 15f, -15f, 15f, -15f, 0f).setDuration(400).start(); }catch(Exception e){}
                    if(wrongPasswordCount[0] >= 3) { wrongPasswordCount[0] = 0; if(vaultPrefs.getBoolean("pref_intruder_enabled", false)) captureIntruderPhoto.run(); }
                }
            }
            else if(pinMode[0] == 2) {
                if(entered.equals(savedPin)) { pinMode[0] = 3; showPinDialog.run(); } else { pinInput.setText(""); pinTitle.setText(tr.get("pin_err")); pinTitle.setTextColor(0xFFFF4444); try{ android.animation.ObjectAnimator.ofFloat(pinCard, "translationX", 0f, 15f, -15f, 15f, -15f, 0f).setDuration(400).start(); }catch(Exception e){} }
            }
            else if(pinMode[0] == 3) { tempPin[0] = entered; pinMode[0] = 5; showPinDialog.run(); }
            else if(pinMode[0] == 5) {
                if(entered.equals(tempPin[0])) { vaultPrefs.edit().putString("vault_pin", entered).apply(); pinCancelBtn.performClick(); android.widget.Toast.makeText(ctx, tr.get("pin_ok"), 0).show(); } else { pinInput.setText(""); pinTitle.setText(tr.get("pin_no_match")); pinTitle.setTextColor(0xFFFF4444); try{ android.animation.ObjectAnimator.ofFloat(pinCard, "translationX", 0f, 15f, -15f, 15f, -15f, 0f).setDuration(400).start(); }catch(Exception e){} }
            }
        }
    });
    calcDisplay.setOnLongClickListener(new android.view.View.OnLongClickListener() { @Override public boolean onLongClick(android.view.View v) { ui.bounce(v); tools.vibe(100); pinMode[0] = vaultPrefs.getString("vault_pin", "").isEmpty() ? 0 : 1; showPinDialog.run(); return true; }});
    if (btn9Holder[0] != null) {
        btn9Holder[0].setOnLongClickListener(new android.view.View.OnLongClickListener() { @Override public boolean onLongClick(android.view.View v) { ui.bounce(v); tools.vibe(100); vaultPrefs.edit().putBoolean("hint_9_seen", true).apply(); if(vaultPrefs.getString("vault_pin", "").isEmpty()) { android.widget.Toast.makeText(ctx, tr.get("pin_no_pass"), 0).show(); } else { pinMode[0] = 2; showPinDialog.run(); } return true; } });
    }
    final Runnable lockApp = new Runnable() {
        public void run() {
            if(vaultLayer.getVisibility() == android.view.View.VISIBLE || settingsLayer.getVisibility() == android.view.View.VISIBLE || supportLayer.getVisibility() == android.view.View.VISIBLE) {
                vaultLayer.setVisibility(android.view.View.GONE); fakeUiLayer.setVisibility(android.view.View.VISIBLE); settingsLayer.setVisibility(android.view.View.GONE); editLayer.setVisibility(android.view.View.GONE); delLayer.setVisibility(android.view.View.GONE); tutLayer.setVisibility(android.view.View.GONE); infoLayer.setVisibility(android.view.View.GONE); setIntroLayer.setVisibility(android.view.View.GONE); supportLayer.setVisibility(android.view.View.GONE);
            }
            try {
                if(vaultPrefs.getBoolean("pref_dt_enabled", false)) {
                    long limit = vaultPrefs.getLong("pref_dt_limit", 0);
                    if(limit > 0) {
                        android.app.AlarmManager am = (android.app.AlarmManager) ctx.getSystemService(android.content.Context.ALARM_SERVICE);
                        android.content.Intent intent = new android.content.Intent(ctx, MainActivity.class);
                        intent.putExtra("is_death_timer_wakeup", true);
                        int flags = android.os.Build.VERSION.SDK_INT >= 23 ? android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE : android.app.PendingIntent.FLAG_UPDATE_CURRENT;
                        android.app.PendingIntent pi = android.app.PendingIntent.getActivity(ctx, 1001, intent, flags);
                        if(am != null) { if (android.os.Build.VERSION.SDK_INT >= 23) { am.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + limit, pi); } else { am.setExact(android.app.AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + limit, pi); } }
                    }
                }
            } catch(Exception e){}
            updateProAudio.run();
        }
    };
    final android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) ctx.getSystemService(android.content.Context.SENSOR_SERVICE); final android.hardware.Sensor accelerometer = sensorManager != null ? sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER) : null; final long[] lastShakeTime = {0};
    final android.hardware.SensorEventListener secListener = new android.hardware.SensorEventListener() { private float mAccel = 0.00f; private float mAccelCurrent = android.hardware.SensorManager.GRAVITY_EARTH; private float mAccelLast = android.hardware.SensorManager.GRAVITY_EARTH; @Override public void onSensorChanged(android.hardware.SensorEvent event) { if(vaultLayer.getVisibility() == android.view.View.VISIBLE) { int al = vaultPrefs.getInt("pref_auto_lock", 0); if(al > 0 && (System.currentTimeMillis() - lastInteraction[0] > (al * 1000))) { lockApp.run(); return; } float x = event.values[0]; float y = event.values[1]; float z = event.values[2]; mAccelLast = mAccelCurrent; mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z)); float delta = mAccelCurrent - mAccelLast; mAccel = mAccel * 0.9f + delta; if (mAccel > 12 && vaultPrefs.getBoolean("pref_shake", false)) { long now = System.currentTimeMillis(); if (now - lastShakeTime[0] > 1000) { lastShakeTime[0] = now; tools.vibe(150); lockApp.run(); } } } } @Override public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {} };
    if (accelerometer != null) sensorManager.registerListener(secListener, accelerometer, android.hardware.SensorManager.SENSOR_DELAY_UI);
    vaultLayer.setOnTouchListener(new android.view.View.OnTouchListener() { @Override public boolean onTouch(android.view.View v, android.view.MotionEvent event) { lastInteraction[0] = System.currentTimeMillis(); return false; } });
    final android.app.Application app = (android.app.Application) ctx.getApplicationContext();
    final android.app.Application.ActivityLifecycleCallbacks lifecycleCallbacks = new android.app.Application.ActivityLifecycleCallbacks() {
        @Override public void onActivityCreated(android.app.Activity a, android.os.Bundle b) {} @Override public void onActivityStarted(android.app.Activity a) {}
        @Override public void onActivityResumed(android.app.Activity a) {
            if(a == ctx) {
                lastInteraction[0] = System.currentTimeMillis();
                try { android.app.AlarmManager am = (android.app.AlarmManager) ctx.getSystemService(android.content.Context.ALARM_SERVICE); android.content.Intent intent = new android.content.Intent(ctx, MainActivity.class); int flags = android.os.Build.VERSION.SDK_INT >= 23 ? android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE : android.app.PendingIntent.FLAG_UPDATE_CURRENT; android.app.PendingIntent pi = android.app.PendingIntent.getActivity(ctx, 1001, intent, flags); if(am != null) am.cancel(pi); } catch(Exception e){}
                updateProAudio.run();
                if(false) { // removed camera intro
                    boolean camGranted2 = false;
                    if(camGranted2) {
                        vaultPrefs.edit().putBoolean("pref_intro_shown", true).apply();
                        new android.os.Handler().postDelayed(new Runnable() { public void run() {
                            try {
                                                                final android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(ctx);
                                android.widget.TextView tv = new android.widget.TextView(ctx);
                                tv.setText(tr.get("cam_grant_msg"));
                                tv.setTextSize(16f); tv.setTextColor(0xFF111111); tv.setPadding(48, 40, 48, 20); tv.setLineSpacing(6f, 1.3f); tv.setGravity(android.view.Gravity.CENTER);
                                b.setView(tv);
                                b.setPositiveButton("تێگەیشتم ✅", null);
                                final android.app.AlertDialog[] dHolder = {b.create()}; dHolder[0].show();
                                android.widget.Button btn = dHolder[0].getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                                if(btn != null) { btn.setTextSize(15f); btn.setTextColor(0xFF007AFF); }
                                new android.os.Handler().postDelayed(new Runnable() { public void run() { try { if(dHolder[0].isShowing()) dHolder[0].dismiss(); } catch(Exception e2){} } }, 5000);
                            } catch(Exception e) {}
                        }}, 700);
                    }
                }
            }
        }
        @Override public void onActivityPaused(android.app.Activity a) { if(a == ctx) { lockApp.run(); } }
        @Override public void onActivityStopped(android.app.Activity a) {} @Override public void onActivitySaveInstanceState(android.app.Activity a, android.os.Bundle b) {}
        @Override public void onActivityDestroyed(android.app.Activity a) {
            if(a == ctx) {
                try { if(sensorManager != null && secListener != null) sensorManager.unregisterListener(secListener); } catch(Exception e){}
                try { app.unregisterActivityLifecycleCallbacks(this); } catch(Exception e){}
                try { if(proMediaPlayer[0] != null) { proMediaPlayer[0].release(); proMediaPlayer[0] = null; } } catch(Exception e){}
            }
        }
    };
    app.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    for(Runnable r : uiUpdaters) r.run();
    masterCanvas.addView(vaultLayer);
    masterCanvas.addView(fakeUiLayer);
    masterCanvas.addView(editLayer);
    masterCanvas.addView(settingsLayer);
    masterCanvas.addView(delLayer);
    masterCanvas.addView(pinDialogLayer);
    masterCanvas.addView(infoLayer);
    masterCanvas.addView(tutLayer);
    masterCanvas.addView(hint9Layer);
    masterCanvas.addView(setIntroLayer);
    masterCanvas.addView(supportLayer);
    masterCanvas.addView(intruderLogLayer);
    masterCanvas.addView(readModeLayer);
    fakeUiLayer.post(new Runnable() {
        public void run() {
            try {
                int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
                boolean isDay = (hour >= 6 && hour < 18);
                if(!vaultPrefs.contains("pref_vault_design")) {
                    int firstDesign = isDay ? 3 : 2;
                    vaultPrefs.edit().putInt("pref_vault_design", firstDesign).apply();
                    for(Runnable r : uiUpdaters) r.run();
                } else if(vaultPrefs.getBoolean("pref_auto_theme", false)) {
                    int autoDesign = isDay ? 3 : 2;
                    int curDesign = vaultPrefs.getInt("pref_vault_design", 0);
                    if(curDesign != autoDesign) {
                        vaultPrefs.edit().putInt("pref_vault_design", autoDesign).apply();
                        for(Runnable r : uiUpdaters) r.run();
                    }
                }
            } catch(Exception e) {}

            if(!vaultPrefs.getBoolean("tut_calc_seen", false)) {
                inVaultTut[0] = false; tutStep[0] = 0; updateTutUI.run();
                tutLayer.setVisibility(android.view.View.VISIBLE); tutLayer.setAlpha(0f);
                try{ android.animation.ObjectAnimator.ofFloat(tutLayer, "alpha", 0f, 1f).setDuration(400).start(); }catch(Exception e){}
            }

        }
    });
} catch (Exception fatalError) {
    android.widget.Toast.makeText(MainActivity.this, "Error: " + fatalError.getMessage(), android.widget.Toast.LENGTH_LONG).show();
}
