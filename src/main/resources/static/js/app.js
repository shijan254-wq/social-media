// ============================================
// APP.JS — Feed, Profile, Posts, Dark Mode, Edit Post, Emoji
// ============================================

// ============================================
// DARK MODE
// ============================================
function initDarkMode() {
    const isDark = localStorage.getItem('darkMode') === 'true';
    if (isDark) {
        document.body.classList.add('dark-mode');
        updateDarkIcon(true);
    }
}

function toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    const isDark = document.body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', isDark);
    updateDarkIcon(isDark);
}

function updateDarkIcon(isDark) {
    const btn = document.getElementById('darkToggle');
    if (btn) {
        btn.innerHTML = isDark 
            ? '<i class="fas fa-sun"></i>' 
            : '<i class="fas fa-moon"></i>';
    }
}

// ============================================
// EMOJI PICKER
// ============================================
const EMOJIS = {
    'Hisi': ['😀', '😃', '😄', '😁', '😆', '😅', '🤣', '😂', '🙂', '🙃', '😉', '😊', '😇', '🥰', '😍', '🤩', '😘', '😗', '😚', '😙'],
    'Nyuso': ['😐', '😑', '😶', '🤐', '🤨', '😏', '😒', '🙄', '😬', '🤥', '😌', '😔', '😪', '🤤', '😴', '😷', '🤒', '🤕', '🤢', '🤮'],
    'Mikono': ['👍', '👎', '👌', '✌️', '🤞', '🤟', '🤘', '🤙', '👈', '👉', '👆', '👇', '☝️', '👋', '🤚', '🖐️', '✋', '🖖', '👏', '🙌'],
    'Moyo': ['❤️', '🧡', '💛', '💚', '💙', '💜', '🖤', '🤍', '🤎', '💔', '❣️', '💕', '💞', '💓', '💗', '💖', '💘', '💝', '💟', '♥️'],
    'Wanyama': ['🐶', '🐱', '🐭', '🐹', '🐰', '🦊', '🐻', '🐼', '🐨', '🐯', '🦁', '🐮', '🐷', '🐸', '🐵', '🐔', '🐧', '🐦', '🐤', '🦆'],
    'Chakula': ['🍎', '🍊', '🍋', '🍌', '🍉', '🍇', '🍓', '🍈', '🍒', '🍑', '🥭', '🍍', '🥥', '🥝', '🍅', '🥑', '🍆', '🥔', '🥕', '🌽'],
    'Shughuli': ['⚽', '🏀', '🏈', '⚾', '🎾', '🏐', '🏉', '🎱', '🏓', '🏸', '🥊', '🥋', '🎯', '⛳', '🎣', '🎽', '🛹', '🛼', '🏂', '🏋️'],
    'Alama': ['✅', '❌', '⭐', '🌟', '✨', '⚡', '🔥', '💯', '💢', '💥', '💫', '💦', '💨', '🎉', '🎊', '🎈', '🎁', '🏆', '🥇', '👑']
};

let activeInputForEmoji = null;

function initEmojiPicker() {
    if (document.getElementById('emojiPicker')) return;
    
    const picker = document.createElement('div');
    picker.id = 'emojiPicker';
    picker.style.cssText = `
        position: fixed;
        bottom: 30px;
        right: 30px;
        background: var(--white);
        color: var(--dark);
        border-radius: 15px;
        box-shadow: 0 20px 40px rgba(0,0,0,0.25);
        padding: 15px;
        width: 350px;
        max-height: 400px;
        overflow-y: auto;
        z-index: 9999;
        display: none;
        border: 2px solid var(--border);
    `;
    
    let html = '';
    for (const [category, emojis] of Object.entries(EMOJIS)) {
        html += `
            <div style="margin-bottom: 15px;">
                <div style="font-size: 12px; font-weight: 700; color: var(--gray); 
                            text-transform: uppercase; margin-bottom: 8px; letter-spacing: 0.5px;">
                    ${category}
                </div>
                <div style="display: grid; grid-template-columns: repeat(10, 1fr); gap: 4px;">
                    ${emojis.map(e => `
                        <button type="button" onclick="insertEmoji('${e}')" 
                                style="background: none; border: none; font-size: 22px; 
                                       padding: 4px; border-radius: 6px; cursor: pointer;
                                       transition: all 0.2s;"
                                onmouseover="this.style.background='var(--light-gray)'; this.style.transform='scale(1.2)';"
                                onmouseout="this.style.background='none'; this.style.transform='scale(1)';"
                        >${e}</button>
                    `).join('')}
                </div>
            </div>
        `;
    }
    
    picker.innerHTML = html;
    document.body.appendChild(picker);
    
    document.addEventListener('click', (e) => {
        if (!picker.contains(e.target) && !e.target.closest('.emoji-btn')) {
            picker.style.display = 'none';
        }
    });
}

function toggleEmojiPicker(event, inputId) {
    event.stopPropagation();
    initEmojiPicker();
    
    const picker = document.getElementById('emojiPicker');
    const input = document.getElementById(inputId);
    
    if (!input) return;
    
    activeInputForEmoji = inputId;
    
    if (picker.style.display === 'block') {
        picker.style.display = 'none';
    } else {
        picker.style.display = 'block';
    }
}

function insertEmoji(emoji) {
    if (!activeInputForEmoji) return;
    
    const input = document.getElementById(activeInputForEmoji);
    if (!input) return;
    
    const start = input.selectionStart || input.value.length;
    const end = input.selectionEnd || input.value.length;
    const text = input.value;
    
    input.value = text.substring(0, start) + emoji + text.substring(end);
    
    const newPos = start + emoji.length;
    input.setSelectionRange(newPos, newPos);
    
    input.focus();
    input.dispatchEvent(new Event('input', { bubbles: true }));
}

function emojiButtonHtml(inputId) {
    return `
        <button type="button" class="emoji-btn" onclick="toggleEmojiPicker(event, '${inputId}')" 
                style="background: none; border: 2px solid var(--border); 
                       padding: 8px 12px; border-radius: 10px; font-size: 18px; 
                       cursor: pointer; transition: all 0.2s;"
                title="Ongeza emoji"
        >😊</button>
    `;
}

// ============================================
// NAVBAR
// ============================================
function renderNavbar() {
    const nav = document.getElementById('navbar');
    if (!nav) return;
    
    const user = Auth.getUser();
    const isDark = document.body.classList.contains('dark-mode');
    const darkIcon = isDark ? '<i class="fas fa-sun"></i>' : '<i class="fas fa-moon"></i>';
    
    if (user) {
        nav.innerHTML = `
            <div class="nav-container">
                <a href="/feed.html" class="logo">SIMU<span>.</span></a>
                
                <div class="nav-search">
                    <input type="text" id="navSearch" placeholder="Tafuta watumiaji...">
                </div>
                
                <ul class="nav-links">
                    <li>
                        <button class="dark-toggle" id="darkToggle" onclick="toggleDarkMode()" title="Dark mode">
                            ${darkIcon}
                        </button>
                    </li>
                    <li><a href="/feed.html"><i class="fas fa-home"></i> Feed</a></li>
                    <li>
                        <a href="/profile.html?id=${user.id}" class="nav-user">
                            <span class="nav-avatar">${getInitial(user.fullName || user.username)}</span>
                            <span>${escapeHtml((user.fullName || user.username).split(' ')[0])}</span>
                        </a>
                    </li>
                    <li>
                        <a href="#" onclick="logout(); return false;" title="Ondoka">
                            <i class="fas fa-sign-out-alt"></i>
                        </a>
                    </li>
                </ul>
            </div>
        `;
        
        const searchInput = document.getElementById('navSearch');
        if (searchInput) {
            searchInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    const q = e.target.value.trim();
                    if (q) {
                        window.location.href = `/search.html?q=${encodeURIComponent(q)}`;
                    }
                }
            });
        }
    } else {
        nav.innerHTML = `
            <div class="nav-container">
                <a href="/" class="logo">SIMU<span>.</span></a>
                
                <ul class="nav-links">
                    <li>
                        <button class="dark-toggle" id="darkToggle" onclick="toggleDarkMode()" title="Dark mode">
                            ${darkIcon}
                        </button>
                    </li>
                    <li><a href="/login.html"><i class="fas fa-sign-in-alt"></i> Ingia</a></li>
                    <li>
                        <a href="/register.html" class="btn btn-primary" style="padding: 8px 20px;">
                            <i class="fas fa-user-plus"></i> Jisajili
                        </a>
                    </li>
                </ul>
            </div>
        `;
    }
}

// ============================================
// POST CARD RENDER
// ============================================
function renderPost(post) {
    const user = Auth.getUser();
    const isOwn = user && user.id === post.userId;
    
    let actionsHtml = `
        <button class="post-action ${post.isLiked ? 'liked' : ''}" onclick="toggleLike(${post.id})">
            <i class="fas fa-heart${post.isLiked ? '' : ' far'}"></i> <span>${post.likesCount || 0}</span>
        </button>
        <button class="post-action" onclick="viewPost(${post.id})">
            <i class="fas fa-comment"></i> <span>${post.commentsCount || 0}</span>
        </button>
    `;
    
    if (isOwn) {
        actionsHtml += `
            <button class="post-action" onclick="startEditPost(${post.id})" style="margin-left: auto;">
                <i class="fas fa-edit"></i> Edit
            </button>
            <button class="post-action" onclick="deletePost(${post.id})" style="color: var(--danger);">
                <i class="fas fa-trash"></i>
            </button>
        `;
    }
    
    return `
        <div class="post-card" id="post-${post.id}">
            <div class="post-header">
                <div class="post-avatar">${getInitial(post.fullName || post.username)}</div>
                <div class="post-user-info">
                    <h4>${escapeHtml(post.fullName || post.username)}</h4>
                    <span>@${escapeHtml(post.username)} · ${formatDate(post.createdAt)}</span>
                </div>
            </div>
            
            <div class="post-content" id="post-content-${post.id}">${escapeHtml(post.content)}</div>
            
            ${post.imageUrl ? `<img src="${escapeHtml(post.imageUrl)}" class="post-image" onerror="this.style.display='none'">` : ''}
            
            <div class="post-actions" id="post-actions-${post.id}">
                ${actionsHtml}
            </div>
        </div>
    `;
}

// ============================================
// START EDIT POST
// ============================================
function startEditPost(postId) {
    const postEl = document.getElementById(`post-${postId}`);
    if (!postEl) return;
    
    const contentEl = document.getElementById(`post-content-${postId}`);
    const actionsEl = document.getElementById(`post-actions-${postId}`);
    
    const originalContent = contentEl.textContent;
    const safeContent = escapeHtml(originalContent);
    
    // Badilisha content kuwa textarea + emoji button
    contentEl.innerHTML = `
        <textarea id="edit-textarea-${postId}" 
                  style="width: 100%; padding: 12px 16px; border: 2px solid var(--primary); 
                         border-radius: 10px; font-size: 15px; font-family: inherit; 
                         background: var(--white); color: var(--dark); resize: vertical; 
                         min-height: 80px; outline: none;"
        >${safeContent}</textarea>
        <div style="margin-top: 10px; display: flex; justify-content: flex-end;">
            ${emojiButtonHtml(`edit-textarea-${postId}`)}
        </div>
    `;
    
    // Badilisha actions kuwa save/cancel
    actionsEl.innerHTML = `
        <button class="btn btn-primary" onclick="saveEditPost(${postId})" style="padding: 8px 20px;">
            <i class="fas fa-save"></i> Hifadhi
        </button>
        <button class="btn btn-ghost" onclick="cancelEditPost(${postId})" style="padding: 8px 20px;">
            <i class="fas fa-times"></i> Ghairi
        </button>
    `;
    
    // Focus textarea
    setTimeout(() => {
        const textarea = document.getElementById(`edit-textarea-${postId}`);
        if (textarea) {
            textarea.focus();
            textarea.setSelectionRange(textarea.value.length, textarea.value.length);
        }
    }, 100);
}

// ============================================
// SAVE EDIT POST
// ============================================
async function saveEditPost(postId) {
    const textarea = document.getElementById(`edit-textarea-${postId}`);
    if (!textarea) return;
    
    const newContent = textarea.value.trim();
    
    if (!newContent) {
        return showToast('Post haiwezi kuwa tupu', 'error');
    }
    
    const user = Auth.getUser();
    const actionsEl = document.getElementById(`post-actions-${postId}`);
    
    actionsEl.innerHTML = `
        <button class="btn btn-primary" disabled style="padding: 8px 20px;">
            <i class="fas fa-spinner fa-spin"></i> Inahifadhi...
        </button>
    `;
    
    const res = await API.updatePost(postId, user.id, { content: newContent });
    
    if (res.success) {
        showToast('Post imebadilishwa!', 'success');
        
        const contentEl = document.getElementById(`post-content-${postId}`);
        contentEl.textContent = newContent;
        
        const post = res.data;
        
        actionsEl.innerHTML = `
            <button class="post-action ${post.isLiked ? 'liked' : ''}" onclick="toggleLike(${post.id})">
                <i class="fas fa-heart${post.isLiked ? '' : ' far'}"></i> <span>${post.likesCount || 0}</span>
            </button>
            <button class="post-action" onclick="viewPost(${post.id})">
                <i class="fas fa-comment"></i> <span>${post.commentsCount || 0}</span>
            </button>
            <button class="post-action" onclick="startEditPost(${post.id})" style="margin-left: auto;">
                <i class="fas fa-edit"></i> Edit
            </button>
            <button class="post-action" onclick="deletePost(${post.id})" style="color: var(--danger);">
                <i class="fas fa-trash"></i>
            </button>
        `;
    } else {
        showToast(res.message || 'Imeshindikana', 'error');
        
        actionsEl.innerHTML = `
            <button class="btn btn-primary" onclick="saveEditPost(${postId})" style="padding: 8px 20px;">
                <i class="fas fa-save"></i> Jaribu Tena
            </button>
            <button class="btn btn-ghost" onclick="location.reload()" style="padding: 8px 20px;">
                <i class="fas fa-times"></i> Ghairi
            </button>
        `;
    }
}

// ============================================
// CANCEL EDIT POST
// ============================================
function cancelEditPost(postId) {
    const postEl = document.getElementById(`post-${postId}`);
    if (!postEl) return;
    location.reload();
}

// ============================================
// FEED PAGE
// ============================================
async function initFeedPage() {
    const feedEl = document.getElementById('feed');
    if (!feedEl) return;
    
    if (!Auth.isLoggedIn()) {
        window.location.href = '/login.html';
        return;
    }
    
    const user = Auth.getUser();
    
    // Post creator yenye emoji button
    const creatorEl = document.getElementById('postCreator');
    if (creatorEl) {
        creatorEl.innerHTML = `
            <div class="post-creator" style="flex-wrap: wrap;">
                <div class="post-avatar">${getInitial(user.fullName || user.username)}</div>
                <textarea id="postContent" placeholder="Unafikiria nini, ${escapeHtml(user.fullName || user.username)}?"></textarea>
                <div style="display: flex; gap: 8px; align-self: flex-end;">
                    ${emojiButtonHtml('postContent')}
                    <button class="btn btn-primary" onclick="submitPost()">
                        <i class="fas fa-paper-plane"></i> Post
                    </button>
                </div>
            </div>
        `;
    }
    
    feedEl.innerHTML = `
        <div class="loading">
            <div class="spinner"></div>
            Inapakia...
        </div>
    `;
    
    const res = await API.getAllPosts(0, 20);
    
    if (!res.success) {
        feedEl.innerHTML = `
            <div class="empty">
                <div class="empty-icon"><i class="fas fa-exclamation-triangle"></i></div>
                <h3>Hitilafu</h3>
                <p>${res.message || 'Imeshindikana kupata posts'}</p>
            </div>
        `;
        return;
    }
    
    const posts = res.data.content || [];
    
    if (posts.length === 0) {
        feedEl.innerHTML = `
            <div class="empty">
                <div class="empty-icon"><i class="fas fa-edit"></i></div>
                <h3>Hakuna posts bado</h3>
                <p>Kuwa wa kwanza kuweka post!</p>
            </div>
        `;
        return;
    }
    
    feedEl.innerHTML = posts.map(renderPost).join('');
}

// ============================================
// SUBMIT POST
// ============================================
async function submitPost() {
    const textarea = document.getElementById('postContent');
    const content = textarea.value.trim();
    
    if (!content) {
        return showToast('Andika kitu kwanza', 'error');
    }
    
    const user = Auth.getUser();
    const res = await API.createPost(user.id, { content });
    
    if (res.success) {
        textarea.value = '';
        showToast('Post imeundwa!', 'success');
        
        const feedEl = document.getElementById('feed');
        const html = renderPost(res.data);
        feedEl.insertAdjacentHTML('afterbegin', html);
    } else {
        showToast(res.message || 'Imeshindikana', 'error');
    }
}

// ============================================
// TOGGLE LIKE
// ============================================
async function toggleLike(postId) {
    const user = Auth.getUser();
    const res = await API.toggleLike(postId, user.id);
    
    if (res.success) {
        const postEl = document.getElementById(`post-${postId}`);
        if (postEl) {
            const btn = postEl.querySelector('.post-action');
            btn.classList.toggle('liked', res.data.liked);
            btn.innerHTML = `<i class="fas fa-heart${res.data.liked ? '' : ' far'}"></i> <span>${res.data.likesCount}</span>`;
        }
    } else {
        showToast(res.message || 'Imeshindikana', 'error');
    }
}

// ============================================
// DELETE POST
// ============================================
async function deletePost(postId) {
    if (!confirm('Una uhakika kufuta post hii?')) return;
    
    const user = Auth.getUser();
    const res = await API.deletePost(postId, user.id);
    
    if (res.success) {
        const postEl = document.getElementById(`post-${postId}`);
        if (postEl) {
            postEl.style.opacity = '0';
            postEl.style.transform = 'translateX(-100%)';
            setTimeout(() => postEl.remove(), 300);
        }
        showToast('Post imefutwa', 'success');
    } else {
        showToast(res.message || 'Imeshindikana', 'error');
    }
}

// ============================================
// VIEW POST
// ============================================
function viewPost(postId) {
    window.location.href = `/post.html?id=${postId}`;
}

// ============================================
// PROFILE PAGE
// ============================================
async function initProfilePage() {
    const profileEl = document.getElementById('profile');
    if (!profileEl) return;
    
    const params = new URLSearchParams(window.location.search);
    const userId = params.get('id');
    
    if (!userId) {
        window.location.href = '/feed.html';
        return;
    }
    
    const currentUser = Auth.getUser();
    
    const userRes = await API.getUser(userId);
    
    if (!userRes.success) {
        profileEl.innerHTML = `
            <div class="empty">
                <div class="empty-icon"><i class="fas fa-user-slash"></i></div>
                <h3>Mtumiaji haipatikani</h3>
            </div>
        `;
        return;
    }
    
    const profileUser = userRes.data;
    const isOwn = currentUser && currentUser.id == profileUser.id;
    
    let actionBtn = '';
    if (!isOwn && currentUser) {
        actionBtn = `
            <button class="btn ${profileUser.isFollowing ? 'btn-ghost' : 'btn-primary'}" onclick="toggleFollow(${profileUser.id})" id="followBtn">
                ${profileUser.isFollowing 
                    ? '<i class="fas fa-check"></i> Unamfuata' 
                    : '<i class="fas fa-plus"></i> Mfuate'}
            </button>
        `;
    } else if (isOwn) {
        actionBtn = `
            <a href="/settings.html" class="btn btn-ghost">
                <i class="fas fa-edit"></i> Badilisha Profile
            </a>
        `;
    }
    
    profileEl.innerHTML = `
        <div class="profile-header">
            <div class="profile-avatar-lg">${getInitial(profileUser.fullName || profileUser.username)}</div>
            <h1>${escapeHtml(profileUser.fullName || profileUser.username)}</h1>
            <div class="username">@${escapeHtml(profileUser.username)}</div>
            ${profileUser.bio ? `<div class="bio">${escapeHtml(profileUser.bio)}</div>` : ''}
            
            <div class="profile-stats">
                <div class="profile-stat">
                    <div class="num">${profileUser.postsCount || 0}</div>
                    <div class="label">Posts</div>
                </div>
                <div class="profile-stat">
                    <div class="num">${profileUser.followersCount || 0}</div>
                    <div class="label">Followers</div>
                </div>
                <div class="profile-stat">
                    <div class="num">${profileUser.followingCount || 0}</div>
                    <div class="label">Following</div>
                </div>
            </div>
            
            ${actionBtn}
        </div>
        
        <div id="userPosts">
            <div class="loading">
                <div class="spinner"></div>
                Inapakia posts...
            </div>
        </div>
    `;
    
    const postsRes = await API.getUserPosts(userId, 0, 20);
    const postsEl = document.getElementById('userPosts');
    
    if (!postsRes.success) {
        postsEl.innerHTML = `
            <div class="empty">
                <div class="empty-icon"><i class="fas fa-exclamation-triangle"></i></div>
                <h3>Hitilafu</h3>
            </div>
        `;
        return;
    }
    
    const posts = postsRes.data.content || [];
    
    if (posts.length === 0) {
        postsEl.innerHTML = `
            <div class="empty">
                <div class="empty-icon"><i class="fas fa-edit"></i></div>
                <h3>Hakuna posts bado</h3>
            </div>
        `;
        return;
    }
    
    postsEl.innerHTML = posts.map(renderPost).join('');
}

// ============================================
// TOGGLE FOLLOW
// ============================================
async function toggleFollow(userId) {
    const currentUser = Auth.getUser();
    const res = await API.toggleFollow(userId, currentUser.id);
    
    if (res.success) {
        const btn = document.getElementById('followBtn');
        if (btn) {
            btn.className = `btn ${res.data.following ? 'btn-ghost' : 'btn-primary'}`;
            btn.innerHTML = res.data.following 
                ? '<i class="fas fa-check"></i> Unamfuata' 
                : '<i class="fas fa-plus"></i> Mfuate';
        }
        showToast(res.message, 'success');
    } else {
        showToast(res.message, 'error');
    }
}

// ============================================
// ANZISHA
// ============================================
document.addEventListener('DOMContentLoaded', () => {
    initDarkMode();
    renderNavbar();
    initFeedPage();
    initProfilePage();
});
