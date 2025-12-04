document.addEventListener('DOMContentLoaded', function() {
    const navbar = document.getElementById('mainNavbar');
    if (!navbar) return;

    function toggleNavbar() {
        if (window.scrollY > 50) { 
            navbar.classList.remove('header-transparent');
            navbar.classList.add('header-scrolled');
        } else {
            // Vuelve al estado transparente en la parte superior
            navbar.classList.remove('header-scrolled');
            navbar.classList.add('header-transparent');
        }
    }

    window.addEventListener('scroll', toggleNavbar);
    toggleNavbar(); 
});