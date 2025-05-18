@extends('layouts.app')

@section('content')
<div class="msjk-container mt-4">

    <div class="card">
        <div class="card-body">
            <div class="msjk-category-block">
                <h2 class="msjk-singleproduct-heading">{{ $category->name }}</h2>
                <div class="msjk-single-image-category">

                    @if($category->photo)
                        <img src="{{ asset('storage/' . $category->photo) }}">
                    @else
                        <p>No image available</p>
                    @endif
                </div>
            </div>
            <div class="msjk-edit-del-back-content d-flex">
                <a href="{{ route('category.index') }}" class="btn btn-secondary mt-3">Back to List</a>
                <a href="{{ route('category.edit', $category->id) }}" class="btn btn-primary mt-3">Edit Category</a>

                <form action="{{ route('category.destroy', $category->id) }}" method="POST" class="mt-3">
                    @method('DELETE')
                    @csrf
                    <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this item?')">Delete Product</button>
                </form>
            </div>
        </div>
    </div>
</div>
@endsection
