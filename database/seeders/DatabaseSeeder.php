<?php

namespace Database\Seeders;

use App\Models\User;
// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        // User::factory(10)->create();

        // User::factory()->create([
        //     'name' => 'Test User',
        //     'email' => 'test@example.com',
        // ]);
        // database lai seeder use gar vaneko
        $this->call([
            RoleSeeder::class
        ]);
        
        $user = User::factory()->create([
            'name' => 'admin',
            'email' => 'admin@rental.com',   
            'password'=> Hash::make('password'),
            'phonenumber' => '9865206679'
        ]);

        $user->assignRole('admin');

        
    }
}
