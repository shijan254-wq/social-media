// ============================================
// AUTH.JS - Login/Register
// ============================================

// ============================================
// UKURASA WA REGISTER
// ============================================
function initRegisterPage() {
    const form = document.getElementById('registerForm');
    if (!form) return;
    
    // HAKUNA auto-redirect - kila mtu anaweza kufungua ukurasa huu
    
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirm = document.getElementById('confirmPassword').value;
        const fullName = document.getElementById('fullName').value.trim();
        
        // Validation
        if (!username || username.length < 3) {
            return showMessage('Username iwe herufi 3+', 'error');
        }
        if (!email) {
            return showMessage('Email inahitajika', 'error');
        }
        if (password.length < 6) {
            return showMessage('Password iwe herufi 6+', 'error');
        }
        if (password !== confirm) {
            return showMessage('Password hazifanani', 'error');
        }
        if (!fullName) {
            return showMessage('Jina kamili linahitajika', 'error');
        }
        
        const btn = document.getElementById('submitBtn');
        btn.disabled = true;
        btn.textContent = 'Inasajili...';
        
        const res = await API.register({
            username,
            email,
            password,
            fullName
        });
        
        if (res.success) {
            form.innerHTML = `
                <div style="text-align: center; padding: 20px 0;">
                    <div style="font-size: 70px; margin-bottom: 20px;">🎉</div>
                    <h3 style="font-size: 22px; font-weight: 800; margin-bottom: 12px; color: var(--dark);">
                        Usajili Umefanikiwa!
                    </h3>
                    <p style="color: var(--gray); margin-bottom: 25px; font-size: 15px; line-height: 1.6;">
                        Akaunti yako imeundwa kikamilifu.<br>
                        Sasa unaweza kuingia kwenye mfumo.
                    </p>
                    <a href="/login.html" class="btn btn-primary btn-full" style="padding: 16px; font-size: 16px;">
                        🔐 Ingia Sasa
                    </a>
                </div>
            `;
        } else {
            showMessage(res.message || 'Usajili umeshindikana', 'error');
            btn.disabled = false;
            btn.textContent = 'Jisajili';
        }
    });
}

// ============================================
// UKURASA WA LOGIN
// ============================================
function initLoginPage() {
    const form = document.getElementById('loginForm');
    if (!form) return;
    
    // HAKUNA auto-redirect - kila mtu anaweza kufungua ukurasa huu
    
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        
        if (!username || !password) {
            return showMessage('Jaza sehemu zote', 'error');
        }
        
        const btn = document.getElementById('submitBtn');
        btn.disabled = true;
        btn.textContent = 'Inaingia...';
        
        const res = await API.login(username, password);
        
        if (res.success) {
            Auth.saveToken(res.data.token);
            Auth.saveUser(res.data);
            
            form.innerHTML = `
                <div style="text-align: center; padding: 20px 0;">
                    <div style="font-size: 70px; margin-bottom: 20px;">✅</div>
                    <h3 style="font-size: 22px; font-weight: 800; margin-bottom: 12px; color: var(--dark);">
                        Karibu Tena!
                    </h3>
                    <p style="color: var(--gray); margin-bottom: 25px; font-size: 15px; line-height: 1.6;">
                        Umeingia kikamilifu.<br>
                        Bonyeza hapa chini kuendelea.
                    </p>
                    <a href="/feed.html" class="btn btn-primary btn-full" style="padding: 16px; font-size: 16px;">
                        🏠 Endelea kwenye Feed
                    </a>
                </div>
            `;
        } else {
            showMessage(res.message || 'Kuingia kumeshindikana', 'error');
            btn.disabled = false;
            btn.textContent = 'Ingia';
        }
    });
}

// ============================================
// SHOW MESSAGE
// ============================================
function showMessage(text, type = 'error') {
    const msg = document.getElementById('message');
    if (!msg) {
        alert(text);
        return;
    }
    
    msg.textContent = text;
    msg.className = `msg ${type} show`;
    
    setTimeout(() => {
        msg.classList.remove('show');
    }, 5000);
}

// ============================================
// LOGOUT
// ============================================
function logout() {
    if (confirm('Una uhakika unataka kuondoka?')) {
        Auth.logout();
    }
}

// ============================================
// ANZISHA
// ============================================
document.addEventListener('DOMContentLoaded', () => {
    initRegisterPage();
    initLoginPage();
});