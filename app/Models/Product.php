<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
use PhpParser\Node\Expr\PostDec;

class Product extends Model
{
    use HasFactory;
    protected $fillable = [
        'otp',
        'name',
        'description',
        'amount',
        'photo',
        'category_id',
        'purchase_date',
    ];
    public function category(): BelongsTo
    {
        return $this->belongsTo(Category::class);
    }

    public function comments()
    {
        return $this->hasMany(Comment::class);
    }
    public function wishlists()
    {
        return $this->belongsToMany(User::class, 'wishlists');
    }

    public function product()
    {
        return $this->belongsTo(User::class);
    }
}
