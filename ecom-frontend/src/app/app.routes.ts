import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guards';
import { adminGuard } from './core/guards/admin.guard';
import { AdminCouponComponent } from './pages/admin/admin-coupon';
import { GiftComponent } from './pages/coupon/gift-coupon/gift';

export const routes: Routes = [

  // AUTH
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

  // PUBLIC / USER
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

  // USER FEATURES
  {
    path: 'cart',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/cart/cart').then(m => m.Cart)
  },
  { path: 'gift', component: GiftComponent, canActivate:[authGuard] }
 , {
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

  // ✅ ACCOUNT (ONLY ONE DEFINITION — FIXED)
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

  // ✅ ADMIN (STRICT)
  {
    path: 'admin',
    canActivate: [adminGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./pages/dashboard/dashboard')
            .then(m => m.DashboardComponent)
      },
      {
        path: 'orders',
        loadComponent: () =>
          import('./pages/admin/admin-orders')
            .then(m => m.AdminOrdersComponent)
      },
      { path: 'coupon', component: AdminCouponComponent }
    ]
  },

  // DEFAULT
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