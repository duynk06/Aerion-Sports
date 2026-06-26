<template>
  <div class="bg-white min-h-screen py-10">
    <div class="container mx-auto px-5 max-w-6xl">
      <!-- Breadcrumbs -->
      <div class="mb-8 text-sm text-gray-500 flex items-center gap-2">
        <router-link to="/" class="hover:text-primary-color transition-colors">Trang chủ</router-link>
        <span class="text-xs">&gt;</span>
        <span class="text-gray-800 font-medium">Hồ sơ</span>
      </div>

      <h1 class="text-2xl font-bold text-gray-900 mb-6">Hồ sơ của tôi</h1>

      <div class="flex flex-col md:flex-row gap-8">
        <!-- Sidebar (User Info) -->
        <div class="w-full md:w-1/3 lg:w-1/4">
          <div class="bg-white border border-gray-100 text-gray-900 rounded-lg p-6 sticky top-24 shadow-sm">
            <div class="flex flex-col items-center mb-6">
              <div class="w-20 h-20 bg-gray-100 text-gray-700 rounded-full flex items-center justify-center text-3xl font-bold mb-3 shadow-inner">
                D
              </div>
              <h2 class="text-xl font-bold">Duy Quyết</h2>
              <p class="text-sm text-gray-500 mt-1">Clonehi3so1@gmail.com</p>
            </div>
            
            <div class="border-t border-gray-100 pt-5 space-y-4 text-sm">
              <div>
                <p class="text-gray-500 mb-1 text-xs font-semibold uppercase tracking-wider">Mã khách hàng</p>
                <p class="font-bold text-[15px]">KH008</p>
              </div>
              <div>
                <p class="text-gray-500 mb-1 text-xs font-semibold uppercase tracking-wider">Tài khoản</p>
                <p class="font-bold text-[15px]">0868219136</p>
              </div>
              <div>
                <p class="text-gray-500 mb-1 text-xs font-semibold uppercase tracking-wider">Điện thoại</p>
                <p class="font-bold text-[15px]">0868219136</p>
              </div>
              <div>
                <p class="text-gray-500 mb-1 text-xs font-semibold uppercase tracking-wider">Địa chỉ mặc định</p>
                <p class="font-bold text-[13px] leading-relaxed">Thanh Xuân, Hà Nội, Phường Thanh Xuân, Thành phố Hà Nội, Việt Nam</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Main Form Content -->
        <div class="w-full md:w-2/3 lg:w-3/4">
          <div class="bg-white rounded-lg shadow-sm border border-gray-100 p-8">

            <!-- Personal Info -->
            <div class="mb-10">
              <h3 class="text-lg font-bold text-gray-900 mb-6">Thông tin cá nhân</h3>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Họ và tên</label>
                  <input type="text" value="Duy Quyết" class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Tài khoản</label>
                  <input type="text" value="0868219136" disabled class="w-full bg-gray-50 border border-gray-200 text-gray-500 text-sm rounded block p-3 outline-none cursor-not-allowed" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
                  <input type="email" value="Clonehi3so1@gmail.com" class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Số điện thoại</label>
                  <input type="text" value="0868219136" class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Giới tính</label>
                  <select class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none">
                    <option value="Nam" selected>Nam</option>
                    <option value="Nu">Nữ</option>
                    <option value="Khac">Khác</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Ngày sinh</label>
                  <input type="date" value="2001-08-12" class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none" />
                </div>
              </div>
            </div>

            <hr class="border-gray-100 mb-10" />

            <!-- Default Address -->
            <div class="mb-10">
              <h3 class="text-lg font-bold text-gray-900 mb-6">Địa chỉ nhận hàng mặc định</h3>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Tên người nhận</label>
                  <input type="text" value="Quyết" class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">SĐT người nhận</label>
                  <input type="text" value="0868219136" class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none" />
                </div>
              </div>

              <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Quốc gia</label>
                  <select class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none">
                    <option selected>Việt Nam</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Tỉnh / Thành phố</label>
                  <select class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none">
                    <option selected>Thành phố Hà Nội</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">Phường / Xã / Đặc khu</label>
                  <select class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none">
                    <option selected>Phường Thanh Xuân</option>
                  </select>
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Địa chỉ chi tiết</label>
                <input type="text" value="Thanh Xuân, Hà Nội" class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none" />
              </div>
            </div>

            <!-- Action Buttons -->
            <div class="flex items-center gap-4">
              <button class="bg-secondary-color hover:bg-red-600 text-white font-medium rounded text-sm px-8 py-3 transition-colors">
                Lưu hồ sơ
              </button>
              <button class="bg-white border border-gray-300 hover:bg-gray-50 text-gray-700 font-medium rounded text-sm px-8 py-3 transition-colors">
                Tải lại
              </button>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
</style>
