import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { authGuard } from './core/guards/auth.guards';
import { HomeComponent } from './pages/home/home';
import { PaymentComponenet } from './pages/payment/payment';
import { InvoiceComponent } from './pages/Invoice/invoice';
import { AdminOrdersComponent } from './pages/admin/admin-orders';

export const routes: Routes = [

  {
    path: 'login',
    loadComponent: () =>
      import('./pages/login/login')
        .then(m => m.LoginComponent)
  },

  {
    path: 'home',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/home/home')
        .then(m => m.HomeComponent)
  },

  {
    path:'cart',
     canActivate: [authGuard],
    loadComponent:()=>
      import('./pages/cart/cart').then(m=>m.Cart)
  },

  {
    path: 'checkout',
    loadComponent: () =>
      import('./pages/checkout/checkout')
        .then(m => m.CheckoutComponent)
  },
  {
    path:'orders',
     canActivate: [authGuard],
    loadComponent:()=>import('./pages/orders/orders').then(m=>m.OrdersComponent)
  },
  {
  path: 'order-details/:id',
  loadComponent: () =>
    import('./pages/orders/order-details').then(m => m.OrderDetailsComponent)
}
,
{path: 'payment/:id', component:PaymentComponenet  
},{
  path: 'invoice/:id', component:InvoiceComponent
},
  {
    path:'wishlist',
     canActivate: [authGuard],
    loadComponent:()=>import('./pages/wishlist/wishlist').then(m=>m.WishlistComponent)
  },
  {
  path:'order-success/:id',
  loadComponent:()=>import('./pages/orders/order-success').then(m=>m.OrderSuccessComponent)
},{
  path:'admin-orders',component:AdminOrdersComponent
}
,
{
  path:'profile',
  loadComponent:()=>import('./pages/profile/profile').then(m=>m.ProfileComponent)
}
  ,
 {
  path: 'product/:id',
  loadComponent: () =>
    import('./pages/product-details/product-details')
      .then(m => m.ProductDetailsComponent)
},
{
  path: 'register',
  loadComponent: () =>
    import('./pages/register/register')
      .then(m => m.RegisterComponent)
}
,
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
