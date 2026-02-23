import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guards';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [

  {
    path: 'login',
    loadComponent: () =>
      import('./pages/login/login').then(m => m.LoginComponent)
  },

  {
    path: 'register',
    loadComponent: () =>
      import('./pages/register/register').then(m => m.RegisterComponent)
  },

  {
    path: 'home',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/home/home').then(m => m.HomeComponent)
  },

  {
    path: 'product/:id',
    loadComponent: () =>
      import('./pages/product-details/product-details')
        .then(m => m.ProductDetailsComponent)
  },

  {
    path: 'cart',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/cart/cart').then(m => m.Cart)
  },

  {
    path: 'checkout',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/checkout/checkout')
        .then(m => m.CheckoutComponent)
  },

  {
    path: 'order-success/:id',
    loadComponent: () =>
      import('./pages/orders/order-success')
        .then(m => m.OrderSuccessComponent)
  },

  {
    path: 'order-details/:id',
    loadComponent: () =>
      import('./pages/orders/order-details')
        .then(m => m.OrderDetailsComponent)
  },

  {
    path: 'payment/:id',
    loadComponent: () =>
      import('./pages/payment/payment')
        .then(m => m.PaymentComponenet)
  },

  {
    path: 'invoice/:id',
    loadComponent: () =>
      import('./pages/Invoice/invoice')
        .then(m => m.InvoiceComponent)
  },

  {
    path: 'admin-orders',
    loadComponent: () =>
      import('./pages/admin/admin-orders')
        .then(m => m.AdminOrdersComponent)
  },
   {
  path: 'dashboard',
  canActivate: [adminGuard], // optional but recommended
  loadComponent: () =>
    import('./pages/dashboard/dashboard')
      .then(m => m.DashboardComponent)
},

  // ✅ MAIN ACCOUNT SECTION (IMPORTANT)
  {
  path: 'account',
  canActivate: [authGuard],
  loadComponent: () =>
    import('./pages/account-layout/account-layout')
      .then(m => m.AccountLayout),
  children: [
    {
      path: 'profile',
      loadComponent: () =>
        import('./pages/profile/profile')
          .then(m => m.ProfileComponent)
    },
    {
      path: 'orders',
      loadComponent: () =>
        import('./pages/orders/orders')
          .then(m => m.OrdersComponent)
    },
   
    {
      path: 'wishlist',
      loadComponent: () =>
        import('./pages/wishlist/wishlist')
          .then(m => m.WishlistComponent)
    },
    {
      path: '',
      redirectTo: 'profile',
      pathMatch: 'full'
    }
  ]
},

  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },

  {
    path: '**',
    redirectTo: 'home'
  }
];