<div id="layoutSidenav">
    <div id="layoutSidenav_nav">
        <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
            <div class="sb-sidenav-menu">
                <div class="nav">
                    {{-- <div class="sb-sidenav-menu-heading">Core</div> --}}
                    <a class="nav-link" href="{{route('dashboard')}}">
                        <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                        Dashboard
                    </a>
                    <a class="nav-link" href="{{route('user.index')}}">
                        <div class="sb-nav-link-icon"><i class="fas fa-users"></i></div>
                        User
                    </a>
                    <a class="nav-link" href="{{route('product.index')}}">
                        <div class="sb-nav-link-icon"><i class="fas fa-box"></i></div>
                        Product
                    </a>
                    <a class="nav-link" href="{{route('category.index')}}">
                        <div class="sb-nav-link-icon"><i class="fas fa-tags"></i></div>
                        Category
                    </a>
                    <a class="nav-link" href="{{route('orders.index')}}">
                        <div class="sb-nav-link-icon"><i class="fas fa-receipt"></i></div>
                        Order
                    </a>
                    <a class="nav-link" href="{{route('orderdetails.index')}}">
                        <div class="sb-nav-link-icon"><i class="fas fa-file-alt"></i></div>
                        Order Details
                    </a>
                    <a class="nav-link" href="{{route('comment.index')}}">
                        <div class="sb-nav-link-icon"><i class="fas fa-comments"></i></div>
                        Comment
                    </a>
                    
                </div>
            </div>
            <div class="sb-sidenav-footer">
                <div class="small">Logged in as:</div>
                {{ Auth::user()->name }}
            </div>
        </nav>
    </div>