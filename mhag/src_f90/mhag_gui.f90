subroutine init_gui
   use mhag
   implicit none

   data_dir="./"
   method=1
   outform=1
   fileout=''
   filein=''
   io_unit=3
   fileinfo='log'
   if_info=.true.

   call read_data
   call data_pre_proc

end subroutine init_gui

! generate menu list : 1-5: armor; 6: charm; 
! 7: 1-slot jewels; 8: 1,2-slot jewel;
! 9: 1,2,3 jewels

subroutine gen_menu_list(lowrank,blader,num_list,menus,slots)
   use mhag
   implicit none
   logical,intent(in) :: lowrank,blader
   integer,intent(out) :: num_list(9)
   character(len=40),dimension(300,9) :: menus
   integer,dimension(300,9) :: slots
   integer :: i,j,n
   type(armor_type) :: armor
   type(charm_type) :: charm
   type(jewel_type) :: jewel
   character(len=255) :: charm_name,jewel_name_short

   num_list=0
   menus=''
   slots=0

   ! armor list
   do i=1,5
      do j=1,num_armor(i)
         armor=armor_list(j,i)
         if((armor%blade_or_gunner.eq."B").and.(.not.blader))cycle
         if((armor%blade_or_gunner.eq."G").and.blader)cycle
         if(lowrank.and.(.not.armor%lowrank))cycle
         num_list(i)=num_list(i)+1
         menus(num_list(i),i)=trim(armor%armor_name)//char(0)
         slots(num_list(i),i)=armor%num_slot
      enddo
   enddo

   ! charm list
   do j=1,num_charm
      charm=charm_list(j)
      if(lowrank.and.(.not.charm%lowrank))cycle
      num_list(6)=num_list(6)+1
      call write_charm_class(j,charm_name)
      menus(num_list(6),6)=trim(charm_name)//char(0)
      slots(num_list(6),6)=charm%num_slot
   enddo

   ! jewel_list
   do j=1,num_jewel
      jewel=jewel_list(j)
      call gen_jewel_name_short(jewel,jewel_name_short)

      n=jewel%num_slot
      if(n.eq.1)then
         num_list(7)=num_list(7)+1
         menus(num_list(7),7)=trim(jewel_name_short)//char(0)
         slots(num_list(7),7)=jewel%num_slot
      endif
      if(n.le.2)then
         num_list(8)=num_list(8)+1
         menus(num_list(8),8)=trim(jewel_name_short)//char(0)
         slots(num_list(8),8)=jewel%num_slot
      endif
      num_list(9)=num_list(9)+1
      menus(num_list(9),9)=trim(jewel_name_short)//char(0)
      slots(num_list(9),9)=jewel%num_slot
   enddo

end subroutine gen_menu_list
