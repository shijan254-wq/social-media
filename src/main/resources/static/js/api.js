// ============================================
// API.JS — Kuwasiliana na Backend
// ============================================

const API_BASE = 'http://localhost:8081/api';

// ============================================
// TOKEN MANAGEMENT
// ============================================
const Auth = {
    saveToken(token) {
        localStorage.setItem('token', token);
    },
    
    getToken() {
        return localStorage.getItem('token');
    },
    
    saveUser(user) {
        localStorage.setItem('user', JSON.stringify(user));
    },
    
    getUser() {
        try {
            return JSON.parse(localStorage.getItem('user'));
        } catch (e) {
            return null;
        }
    },
    
    isLoggedIn() {
        return !!this.getToken();
    },
    
    logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login.html';
    }
};

// ============================================
// API CALL HELPER
// ============================================
async function apiCall(endpoint, options = {}) {
    const url = endpoint.startsWith('http') ? endpoint : `${API_BASE}${endpoint}`;
    
    const token = Auth.getToken();
    
    const config = {
        headers: {
            'Content-Type': 'application/json',
            ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
            ...(options.headers || {})
        },
        ...options
    };
    
    try {
        const res = await fetch(url, config);
        
        // Kama token imeisha
        if (res.status === 401 || res.status === 403) {
            // Unaweza kumtoa mtumiaji hapa
        }
        
        const data = await res.json();
        return data;
    } catch (err) {
        console.error('API Error:', err);
        return { success: false, message: 'Hitilafu ya mtandao: ' + err.message };
    }
}

// ============================================
// API OBJECT
// ============================================
const API = {
    // ============ AUTH ============
    async register(data) {
        return apiCall('/auth/register', {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    
    async login(username, password) {
        return apiCall('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        });
    },
    
    // ============ USERS ============
    async getUser(id) {
        return apiCall(`/users/${id}`);
    },
    
    async updateProfile(id, data) {
        return apiCall(`/users/${id}`, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },
    
    async searchUsers(keyword) {
        return apiCall(`/users/search?q=${encodeURIComponent(keyword)}`);
    },
    
    async getFollowing(id) {
        return apiCall(`/users/${id}/following`);
    },
    
    async getFollowers(id) {
        return apiCall(`/users/${id}/followers`);
    },
    
    // ============ POSTS ============
    async createPost(userId, data) {
        return apiCall(`/posts?userId=${userId}`, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    
    async getPost(id) {
        return apiCall(`/posts/${id}`);
    },
    
    async getAllPosts(page = 0, size = 10) {
        return apiCall(`/posts?page=${page}&size=${size}`);
    },
    
    async getUserPosts(userId, page = 0, size = 10) {
        return apiCall(`/posts/user/${userId}?page=${page}&size=${size}`);
    },
    
    async getFeed(userId, page = 0, size = 10) {
        return apiCall(`/posts/feed?userId=${userId}&page=${page}&size=${size}`);
    },
    
    async updatePost(id, userId, data) {
        return apiCall(`/posts/${id}?userId=${userId}`, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },
    
    async deletePost(id, userId) {
        return apiCall(`/posts/${id}?userId=${userId}`, {
            method: 'DELETE'
        });
    },
    
    // ============ COMMENTS ============
    async createComment(postId, userId, data) {
        return apiCall(`/posts/${postId}/comments?userId=${userId}`, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    
    async getComments(postId) {
        return apiCall(`/posts/${postId}/comments`);
    },
    
    async deleteComment(id, userId) {
        return apiCall(`/comments/${id}?userId=${userId}`, {
            method: 'DELETE'
        });
    },
    
    // ============ LIKES ============
    async toggleLike(postId, userId) {
        return apiCall(`/likes/post/${postId}?userId=${userId}`, {
            method: 'POST'
        });
    },
    
    async hasLiked(postId, userId) {
        return apiCall(`/likes/post/${postId}/user/${userId}`);
    },
    
    // ============ FOLLOW ============
    async toggleFollow(userId, currentUserId) {
        return apiCall(`/follow/${userId}?currentUserId=${currentUserId}`, {
            method: 'POST'
        });
    },
    
    async isFollowing(userId, currentUserId) {
        return apiCall(`/follow/status/${userId}?currentUserId=${currentUserId}`);
    }
};

// ============================================
// UI HELPERS
// ============================================
function showToast(message, type = 'success') {
    let toast = document.getElementById('toast');
    
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast';
        toast.className = 'toast';
        document.body.appendChild(toast);
    }
    
    toast.textContent = message;
    toast.className = `toast ${type} show`;
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diff = (now - date) / 1000; // seconds
    
    if (diff < 60) return 'Sasa hivi';
    if (diff < 3600) return `${Math.floor(diff / 60)}m`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}h`;
    if (diff < 604800) return `${Math.floor(diff / 86400)}d`;
    
    return date.toLocaleDateString('sw-TZ', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

function getInitial(name) {
    if (!name) return 'U';
    return name.charAt(0).toUpperCase();
}