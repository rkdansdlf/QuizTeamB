// Memory Treasures - 상품 목록 페이지 관리 스크립트
class ProductPage {
    constructor() {
        // DOM 요소 캐싱
        this.elements = {
            grid: document.getElementById('productsGrid'),
            resultsCount: document.getElementById('resultsCount'),
            loadingIndicator: document.getElementById('loadingIndicator'),
            emptyState: document.getElementById('emptyState'),
            loadMoreBtn: document.getElementById('loadMoreBtn'),
            searchInput: document.getElementById('searchInput'),
            searchBtn: document.querySelector('.search-btn'), // ID 대신 클래스로 변경
            memoryTypeFilter: document.getElementById('memoryTypeFilter'),
            priceFilter: document.getElementById('priceFilter'),
            ownerFilter: document.getElementById('ownerFilter'),
            emotionFilter: document.getElementById('emotionFilter'),
            emotionDisplay: document.getElementById('emotionDisplay'),
            sortFilter: document.getElementById('sortFilter'),
            activeFilters: document.getElementById('activeFilters'),
            clearFiltersBtn: document.getElementById('clearFiltersBtn'),
            refreshProductsBtn: document.getElementById('refreshProductsBtn')
        };

        // 필요한 요소가 하나라도 없으면 에러를 발생시켜 문제를 빨리 찾도록 함
        for (const key in this.elements) {
            if (!this.elements[key]) {
                console.error(`초기화 실패: HTML에서 id="${key}" 요소를 찾을 수 없습니다.`);
                // 에러 발생 시 더 이상 진행하지 않음
                return;
            }
        }
        
        // 페이지 상태 관리
        this.products = [];
        this.currentPage = 1;
        this.itemsPerPage = 12;
        this.totalProducts = 0;
        this.isLoading = false;
        this.currentFilters = { search: '', memoryType: '', price: '', owner: '', emotion: 5 };

        this.initialize();
    }

    initialize() {
        this.setupEventListeners();
        this.loadInitialProducts();
    }

    setupEventListeners() {
        let searchTimeout;
        this.elements.searchInput.addEventListener('input', () => {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => this.handleFilterChange('search', this.elements.searchInput.value), 500);
        });
        
        this.elements.searchBtn.addEventListener('click', () => this.handleFilterChange('search', this.elements.searchInput.value));
        this.elements.memoryTypeFilter.addEventListener('change', () => this.handleFilterChange('memoryType', this.elements.memoryTypeFilter.value));
        this.elements.priceFilter.addEventListener('change', () => this.handleFilterChange('price', this.elements.priceFilter.value));
        this.elements.ownerFilter.addEventListener('change', () => this.handleFilterChange('owner', this.elements.ownerFilter.value));
        this.elements.emotionFilter.addEventListener('change', () => this.handleFilterChange('emotion', this.elements.emotionFilter.value));
        this.elements.emotionFilter.addEventListener('input', () => this.updateEmotionDisplay());
        
        this.elements.sortFilter.addEventListener('change', () => this.resetAndLoad());
        this.elements.loadMoreBtn.addEventListener('click', () => this.loadMoreProducts());
        this.elements.clearFiltersBtn.addEventListener('click', () => this.clearAllFilters());
        this.elements.refreshProductsBtn.addEventListener('click', () => this.resetAndLoad());
    }

    handleFilterChange(key, value) {
        this.currentFilters[key] = value;
        this.resetAndLoad();
    }

    updateEmotionDisplay() {
        this.elements.emotionDisplay.textContent = `${this.elements.emotionFilter.value} 이상`;
    }

    resetAndLoad() {
        this.currentPage = 1;
        this.products = [];
        this.loadProducts();
    }

    async loadProducts() {
        if (this.isLoading) return;
        this.isLoading = true;
        this.updateUIForLoading(true);

        try {
            const sortValue = this.elements.sortFilter.value;
            const queryParams = new URLSearchParams({
                page: this.currentPage - 1,
                size: this.itemsPerPage,
                sort: sortValue,
            });

            for (const [key, value] of Object.entries(this.currentFilters)) {
                if (value) queryParams.append(key, value);
            }

            const data = await window.memoryTreasures.apiRequest(`/products?${queryParams}`);
            
            this.totalProducts = data.totalElements;
            const newProducts = data.content || [];

            this.products = this.currentPage === 1 ? newProducts : [...this.products, ...newProducts];
            this.render();

        } catch (error) {
            console.error('상품 로딩 중 오류 발생:', error);
            window.memoryTreasures.showToast('상품을 불러오는 데 실패했습니다.', 'error');
        } finally {
            this.isLoading = false;
            this.updateUIForLoading(false);
        }
    }
    
    loadInitialProducts() {
        const urlParams = new URLSearchParams(window.location.search);
        const category = urlParams.get('category');
        if (category) {
            this.elements.memoryTypeFilter.value = category;
            this.currentFilters.memoryType = category;
        }
        this.loadProducts();
    }

    loadMoreProducts() {
        if (this.products.length >= this.totalProducts) return;
        this.currentPage++;
        this.loadProducts();
    }
    
    render() {
        this.renderProductGrid();
        this.updateResultsCount();
        this.updateLoadMoreButton();
        this.renderActiveFilters();
        this.elements.emptyState.style.display = this.products.length === 0 && !this.isLoading ? 'block' : 'none';
    }

    renderProductGrid() {
        const productHtml = this.products.map(product => window.memoryTreasures.createProductCard(product)).join('');
        this.elements.grid.innerHTML = productHtml;
    }

    updateUIForLoading(isLoading) {
        this.elements.loadingIndicator.style.display = isLoading ? 'flex' : 'none';
        this.elements.loadMoreBtn.disabled = isLoading;
        this.elements.loadMoreBtn.textContent = isLoading ? '로딩 중...' : '더 많은 기억 보기';
    }
    
    updateResultsCount() {
        this.elements.resultsCount.innerHTML = `총 <strong>${this.totalProducts}</strong>개의 기억`;
    }

    updateLoadMoreButton() {
        this.elements.loadMoreBtn.style.display = this.products.length >= this.totalProducts ? 'none' : 'block';
    }

    renderActiveFilters() {
        let filtersHtml = '';
        for (const [key, value] of Object.entries(this.currentFilters)) {
            if (value && (key !== 'emotion' || value > 5)) {
                let text = '';
                if (key === 'search' && value) text = `검색: ${value}`;
                else if (key === 'memoryType' && value) text = `종류: ${this.elements.memoryTypeFilter.querySelector(`option[value="${value}"]`).innerText}`;
                else if (key === 'price' && value) text = `가격: ${this.elements.priceFilter.querySelector(`option[value="${value}"]`).innerText}`;
                else if (key === 'owner' && value) text = `주인: ${this.elements.ownerFilter.querySelector(`option[value="${value}"]`).innerText}`;
                else if (key === 'emotion' && value > 5) text = `감정: ${value} 이상`;
                
                if (text) {
                     filtersHtml += `<span class="active-filter-tag" onclick="window.productPage.removeFilter('${key}')">${text} &times;</span>`;
                }
            }
        }
        this.elements.activeFilters.innerHTML = filtersHtml;
    }

    removeFilter(key) {
        this.currentFilters[key] = (key === 'emotion') ? 5 : '';
        if (key === 'search') this.elements.searchInput.value = '';
        if (key === 'memoryType') this.elements.memoryTypeFilter.value = '';
        if (key === 'price') this.elements.priceFilter.value = '';
        if (key === 'owner') this.elements.ownerFilter.value = '';
        if (key === 'emotion') {
            this.elements.emotionFilter.value = 5;
            this.updateEmotionDisplay();
        }
        this.resetAndLoad();
    }

    clearAllFilters() {
        this.currentFilters = { search: '', memoryType: '', price: '', owner: '', emotion: 5 };
        this.elements.searchInput.value = '';
        this.elements.memoryTypeFilter.value = '';
        this.elements.priceFilter.value = '';
        this.elements.ownerFilter.value = '';
        this.elements.emotionFilter.value = 5;
        this.elements.sortFilter.value = 'createdAt,desc';
        this.updateEmotionDisplay();
        this.resetAndLoad();
    }
}

// HTML 문서가 완전히 로드된 후에 ProductPage 인스턴스를 생성합니다.
document.addEventListener('DOMContentLoaded', function() {
    window.productPage = new ProductPage();
});

