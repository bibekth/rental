<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Order extends Model
{
    use HasFactory;

    // Specify the fillable fields
    protected $fillable = [
        'user_id',
        'product_id',
        'order_date',
        'status',
        'quantity',
        'offer',
    ];

    protected $casts = [
        'status' => 'boolean', // Ensures the status is cast to boolean
    ];

   
    // Define relationships
    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function product()
    {
        return $this->belongsTo(Product::class);
    }

    public function orderDetails()
    {
        return $this->hasOne(OrderDetails::class);
    }
}
