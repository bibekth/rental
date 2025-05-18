@extends('layouts.app')
@section('content')
<div class="sa-contact-form-text">
    <h3 class="my-4">Create Category</h3>
    @if ($errors->any())
    <ul>
        @foreach ($errors->all() as $error )
        <li style="color: red"> {{$error}} </li>
        @endforeach  
        
    </ul>
        
    @endif

    <form action="{{ route('category.store' )}}" method="POST" enctype="multipart/form-data">
        @csrf
        <div class="sa-text-field-block">
            <label for="">Category Name</label>
            <input type="text" name="name" id="cnameid" placeholder="Enter category Name">
        </div>
        <div class="sa-text-field-block sa-text-field-block-file">
            <label for="">Upload Image</label>
            <input type="file" name="photo" id="cphotoid">
        </div>
        
        
       
        <div class="sa-form-button">
            <input type="submit" value="Submit">
        </div>
    </form>
</div>
@endsection