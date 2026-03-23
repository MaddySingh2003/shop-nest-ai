import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './footer.html',
})
export class Footer implements OnInit {

  newsletterForm: any;

  constructor(private fb: FormBuilder, private router: Router) {}

  ngOnInit(): void {
    this.newsletterForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  footerSections = [
    {
      title: 'Company',
      links: [
        { label: 'About Us', route: '/about' },
        { label: 'Careers', route: '/careers' },
        { label: 'Blog', route: '/blog' }
      ]
    },
    {
      title: 'Support',
      links: [
        { label: 'Help Center', route: '/help' },
        { label: 'Returns', route: '/returns' },
        { label: 'Track Order', route: '/track-order' }
      ]
    },
    {
      title: 'Sell',
      links: [
        { label: 'Become a Seller', route: '/seller' },
        { label: 'Affiliate Program', route: '/affiliate' },
        { label: 'Advertise', route: '/advertise' }
      ]
    }
  ];

  subscribe() {
    if (this.newsletterForm.valid) {
      const email = this.newsletterForm.value.email;

      console.log('Subscribed:', email);

      alert('Successfully subscribed!');
      this.newsletterForm.reset();
    }
  }

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}