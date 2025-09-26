// Memory Treasures - Main JavaScript Functions
class MemoryTreasures {
    constructor() {
        this.apiBase = '/api';
        this.cart = JSON.parse(localStorage.getItem('memoryCart') || '[]');
        this.user = JSON.parse(localStorage.getItem('currentUser') || 'null');
        this.init();
    }
    
    init() {
        this.updateCartCount();
        this.loadUserInfo();
        window.addEventListener('beforeunload', () => this.saveCartToLocal());
    }
    
    async apiRequest(endpoint, options = {}) {
        const url = this.apiBase + endpoint;
        const config = {
            headers: { 'Content-Type': 'application/json', ...options.headers },
            ...options
        };
        try {
            const response = await fetch(url, config);
            if (!response.ok) throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            return await response.json();
        } catch (error) {
            console.error('API Request failed:', error);
            throw error;
        }
    }
    
    addToCart(product, quantity = 1, event) {
        if (event) {
            event.stopPropagation();
            event.preventDefault();
        }
        const existingItem = this.cart.find(item => item.id === product.id);
        if (existingItem) {
            existingItem.quantity += quantity;
        } else {
            this.cart.push({ ...product, quantity });
        }
        this.saveCartToLocal();
        this.updateCartCount();
        this.showToast(`${product.name}이(가) 장바구니에 추가되었습니다!`, 'success');
        this.animateCartIcon();
    }
    
    saveCartToLocal() {
        localStorage.setItem('memoryCart', JSON.stringify(this.cart));
    }
    
    updateCartCount() {
        const count = this.cart.reduce((total, item) => total + item.quantity, 0);
        document.querySelectorAll('.cart-count').forEach(el => {
            el.textContent = count;
            el.style.display = count > 0 ? 'inline-block' : 'none';
        });
    }

    animateCartIcon() {
        const cartIcon = document.querySelector('.cart-icon');
        if (cartIcon) {
            cartIcon.classList.add('cart-animation');
            setTimeout(() => cartIcon.classList.remove('cart-animation'), 600);
        }
    }
    
    createProductCard(product) {
        const rarityColor = this.getRarityColor(product.rarityScore);
        const emotionEmoji = this.getEmotionEmoji(product.emotionLevel);
        const memoryIcon = this.getMemoryTypeIcon(product.memoryType);
        const productJsonString = JSON.stringify(product).replace(/"/g, '&quot;');

        return `
            <a href="/products/${product.id}" class="product-card-link">
                <div class="product-card" data-product-id="${product.id}">
                    <div class="product-image">
                        ${product.imageUrls && product.imageUrls.length > 0 ? 
                            `<img src="${product.imageUrls[0]}" alt="${product.name}">` :
                            `<div class="no-image"><span class="memory-icon">${memoryIcon}</span></div>`
                        }
                        <div class="rarity-badge" style="background-color: ${rarityColor}">
                            희귀도 ${product.rarityScore}/10
                        </div>
                    </div>
                    <div class="product-info">
                        <div class="product-owner"><span class="owner-tag">${product.originalOwner}의 기억</span></div>
                        <h3 class="product-name">${product.name}</h3>
                        <p class="product-description">${product.description}</p>
                        <div class="emotion-level">
                            <span class="emotion-emoji">${emotionEmoji}</span>
                            <span class="emotion-text">감정 강도: ${product.emotionLevel}/10</span>
                        </div>
                        <div class="product-price">${product.price.toLocaleString()}원</div>
                        <div class="stock-info">${product.stock > 0 ? `<span class="in-stock">재고 ${product.stock}개</span>` : `<span class="out-of-stock">품절</span>`}</div>
                        <div class="product-actions">
                            <button class="add-to-cart-btn" 
                                    onclick="window.memoryTreasures.addToCart(${productJsonString}, 1, event)"
                                    ${product.stock === 0 ? 'disabled' : ''}>
                                ${product.stock > 0 ? '장바구니 담기' : '품절'}
                            </button>
                        </div>
                    </div>
                </div>
            </a>
        `;
    }

    showToast(message, type = 'info', duration = 3000) {
        const container = document.getElementById('toastContainer');
        if (!container) return;
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.textContent = message;
        container.appendChild(toast);
        setTimeout(() => toast.remove(), duration);
    }

    loadUserInfo() { /* ... 이 부분은 기존 코드를 사용하시거나 필요에 맞게 구현 ... */ }
    getRarityColor(score) { if (score >= 9) return '#ff6b6b'; if (score >= 7) return '#4ecdc4'; if (score >= 5) return '#45b7d1'; return '#96ceb4'; }
    getEmotionEmoji(level) { if (level >= 8) return '✨💖'; if (level >= 6) return '😊💫'; if (level >= 4) return '🙂⭐'; return '😐'; }
    getMemoryTypeIcon(type) { const icons = {'CHILDHOOD':'🧸','FRIENDSHIP':'👫','LOVE':'💕','ADVENTURE':'🗺️','FOOD':'🍰','TOY':'🎮','EXPERIMENT':'🧪'}; return icons[type] || '💭'; }
}

document.addEventListener('DOMContentLoaded', () => {
    window.memoryTreasures = new MemoryTreasures();
    
    // a 태그 링크 스타일 제거용 CSS 동적 추가
    const style = document.createElement('style');
    style.textContent = `
        .product-card-link { text-decoration: none !important; color: inherit !important; }
        .product-card-link:hover .product-card { transform: translateY(-5px); box-shadow: 0 8px 25px rgba(0,0,0,0.1); }
        .cart-animation { animation: cart-animation 0.6s ease-in-out; }
        @keyframes cart-animation { 0% { transform: scale(1); } 50% { transform: scale(1.2); } 100% { transform: scale(1); } }
    `;
    document.head.appendChild(style);
});

