! MHAG module
!
! defined data structure : skill_type,armor_type,jewel_type,charm_type
!
! main data list: skill_list,armor_list,jewel_list,charm_list
!
! main subroutines/functions:
! 1. armor skill calculator : 
! subroutine armor_calculator(armor_set)
!
module mhag
   implicit none
   ! skill/armor/jewel/charm data structure
   type set_type
      ! inputs
      character(len=255) :: set_name    ! Name of the set(User defined)
      logical :: lowrank    !lr T/ hr F
      character(len=1) :: blade_or_gunner !B/G 
      integer :: armor_id(5)   !armor id for 5 pieces
      integer :: jewel_id(3,7) !jewel id for 5 pieces,weapon & charm
      integer :: charm_id      !charm id
      integer :: charm_skill_id(2) !two skill ids
      ! outputs
      integer :: defense      !total defense
      integer :: resist(5)    !total resist 
      integer :: num_skill    !all skills involved
      integer :: skill_id(100)       !all skill id
      integer :: skill_point(100)    !all skill points
      integer :: num_effect   !number of effects
      integer :: effect_id(8) !output effect ids (max 8)
      integer :: effect_skill_index(8) !output skill index for the effects(max 8)
      integer :: num_torso    !number of torso up
      integer :: rate         !score: easy to make/slots requirement
   end type set_type

   type skill_type
      character(len=10) :: skill_name
      character(len=1)  :: skill_class        ! A/B/C
      integer :: num_effect                   ! # of effects
      character(len=20),dimension(6) :: effect_name
      integer,dimension(6) :: effect_id
      integer,dimension(6) :: effect_trigger   ! Tigger points
   end type skill_type

   type armor_type
      character(len=40) :: armor_name 
      character(len=1) :: blade_or_gunner,  & ! B/G/A 
                          body_part           ! H/C/A/W/L
      logical :: lowrank                      ! lr Y / hr N
      integer,dimension(2) :: defense         ! 1:lr; 2: hr 
      integer :: num_slot 
      integer,dimension(5) :: resist          ! Fire/Wat/Ice/Thu/Dra
      integer :: num_skill                    ! # of skills
      integer,dimension(5) :: skill_id 
      integer,dimension(5) :: skill_point 
   end type armor_type

   type jewel_type
      character(len=20) :: jewel_name
      logical :: lowrank                      ! lr Y / hr N
      integer :: num_slot 
      integer :: num_skill                    ! 1 or 2
      integer,dimension(2) :: skill_id 
      integer,dimension(2) :: skill_point 
   end type jewel_type

   type charm_type
      integer :: charm_class
      integer :: num_slot
      logical :: lowrank                      ! lr Y / hr N
      integer :: percentage                   ! percentage rate
      integer :: num_skill                    ! # of skills
      character(len=1),dimension(2) :: skill_class     ! A/B/C 
      integer,dimension(2) :: skill_point     ! max points
   end type charm_type

   ! data file

   character(len=255),parameter :: file_armor='armor.dat', &
                                   file_jewel='jewel.dat', &
                                   file_skill='skill.dat', &
                                   file_charm='charm.dat'
   ! MHAG function
   ! io files
   character(len=255),save :: filein    !input file
   character(len=255),save :: fileout   !output(result) file
   character(len=255),save :: fileinfo  !information file
   character(len=255),save :: data_dir  !data file folder
   ! method
   integer,save :: method   !cal/bat/gen/ref
   integer,save :: outform  !1:txt,2:html
   logical,save :: if_info  !print/write info or not
   integer,save :: io_unit  !io_unit
   ! io unit 1: input , 2/6:info ,  3: results

   ! data size limit
   integer,save :: num_jewel,num_skill,num_effect,num_charm
   integer,save :: num_armor(5),max_armor
   integer,save :: num_skill_in_class(5)  !3 in tri;5 reserved for mhf3

   ! data list
   character(len=20),dimension(:),allocatable :: effect_list
   type(skill_type),dimension(:),allocatable :: skill_list
   type(jewel_type),dimension(:),allocatable :: jewel_list
   type(armor_type),dimension(:,:),allocatable :: armor_list
   type(charm_type),dimension(:),allocatable :: charm_list
   integer,dimension(:,:),allocatable :: skill_in_class_list

   ! some parameters
   character(len=255),parameter :: empty_name='-----'

contains

   subroutine mhag_intro 
      integer :: i
      if(if_info)write(io_unit,"(100A)")("=",i=1,100) 
      if(if_info)write(io_unit,"(A)")"MHAG: ver 0.5 "
      if(if_info)write(io_unit,"(A)")"Monster Hunter Armor Generator"
      if(if_info)write(io_unit,"(A)")"By Tifa@mh3, Dec, 2010"
      if(if_info)write(io_unit,"(A)")"http://www.youtube.com/mh3journey"
      if(if_info)write(io_unit,"(100A)")("=",i=1,100) 
      if(if_info)write(io_unit,"(A)")''
   end subroutine

   ! read arguments
   subroutine read_arg
      integer :: i
      character(len=255) :: arg,arg2
      character(len=255),parameter :: filein_default=('input.dat')
      character(len=255),parameter :: fileout_default=('result')

      method=1
      outform=1
      filein=filein_default
      fileout=''
      fileinfo=''
      data_dir='./'
      if_info=.true.
      i=0
      do
         i=i+1
         call getarg(i,arg)
         if(arg.eq.'')then
            exit
         elseif(arg(1:1).eq.'#')then
            i=i+1
            cycle
         else
            select case(trim(arg))
            case ("method","Method","METHOD")
               i=i+1
               call getarg(i,arg2)
               select case(arg2(1:3))
               case default              !("cal","Cal","CAL")
                  method=1               !default
               case ("bat","Bat","BAT")
                  method=2
               case ("gen","Gen","GEN")
                  method=3
               case ("ref","Ref","REF")
                  method=4
               end select
            case ("in","In","IN")
               i=i+1
               call getarg(i,filein)
            case ("out","Out","OUT")
               i=i+1
               call getarg(i,fileout)
            case ("log","Log","LOG")
               i=i+1
               call getarg(i,fileinfo)
            case ("format","Format","FORMAT")
               i=i+1
               call getarg(i,arg2)
               select case(trim(arg2))
               case default  !"text"
                  outform=1
               case ("html")
                  outform=2
               end select
            case ("data","Data","DATA")
               i=i+1
               call getarg(i,arg2)
               if(trim(arg2).eq."")then
                  data_dir="./"
               else
                  data_dir=trim(arg2)//"/"
               endif
            case ("--help","/h","-h")
               call mhag_usage
            case default
               write(6,"(2A)")"Error! incorrect arguments: ",trim(arg)
               call mhag_usage
            end select
         endif
      enddo

      if(filein.eq."")filein=filein_default
      if(fileout.eq."")then
         if(outform.eq.1)then
            fileout=trim(fileout_default)//'.txt'
         else
            fileout=trim(fileout_default)//'.html'
         endif
      endif
      open(unit=3,file=trim(fileout),status='replace')
      if(outform.eq.2)call gen_html_head  !html head 
      

      select case (trim(fileinfo))
      case ("")
         io_unit=6
      case ("off","Off","OFF")
         if_info=.false.
      case default
         io_unit=99
         open(unit=io_unit,file=trim(fileinfo),status='replace')
      end select

   end subroutine read_arg

   ! print mhag usage on screen,and halt program
   subroutine mhag_usage
      write(6,"(A)")"Usage: ./mhag method <cal/bat/gen/ref> in &
         <input.dat> out <result.dat> &
         log <''/log/off> format <text/html> data <./dir>"
      stop
   end subroutine mhag_usage

   subroutine mhag_clean
      if(io_unit.eq.99)close(io_unit)
      if(outform.eq.2)call gen_html_end
      close(3)
      if(allocated(effect_list))deallocate(effect_list)
      if(allocated(skill_list))deallocate(skill_list)
      if(allocated(jewel_list))deallocate(jewel_list)
      if(allocated(armor_list))deallocate(armor_list)
      if(allocated(charm_list))deallocate(charm_list)
      if(allocated(skill_in_class_list))deallocate(skill_in_class_list)
   end subroutine mhag_clean

   ! read set paramters from input file

   subroutine mhag_input_cal(armor_set)
      type(set_type),intent(out) :: armor_set
      character(len=255) :: line,arg,opt
      integer :: eof,pos,id,error,i,nnum,nums(3)
      logical :: fexist,if_end

      if(if_info)write(io_unit,"(A)")"Method: MHAG Set Calculator"
      if(if_info)write(io_unit,"(A)")"Reading User Inputs..."
      call init_set_data(armor_set)

      inquire(file=trim(filein),exist=fexist)
      if(.not.fexist)then
         if(if_info)write(io_unit,"(A)")"Oops, input.dat cannot be found!"
         stop
      endif

      open(unit=1,file=trim(filein),status='old')
      do
         read(1,'(A255)',iostat=eof)line
         if(eof.lt.0)exit
         if((line(1:1).eq."!").or.(line(1:1).eq."#"))cycle
!        print "(2A)","   ",trim(line)

         pos=0
         call extract_word(line,pos,opt,if_end)
         if(if_end)then
            if(if_info)write(io_unit,"(A)")"   Error in Input File, Please Check!"
            stop
         endif
         call extract_word(line,pos,arg,if_end)
         if(.not.if_end)then
            if(if_info)write(io_unit,"(A)")"   Error in Input File, Please Check!"
            stop
         endif

         select case(trim(opt))
         case default
            if(if_info)write(io_unit,"(A)")"   Warning! Parameters not found. Skip!"
            cycle
!         case ("method","METHOD")
!            if((arg(1:1).eq."g").or.(arg(1:1).eq."G"))then
!               method=2
!               print "(A)","   Launch Armor Generator"
!               print "(A)","   Armor Generator doesnot work at this moment!"
!               stop
!            else  ! default
!               method=1
!               print "(A)","   Launch Armor Calculator"
!            endif
         case ("rank")
            if((arg(1:1).eq."l").or.(arg(1:1).eq."L"))then
               armor_set%lowrank=.true.
               if(if_info)write(io_unit,"(A)")"   Low Rank"
            else  ! default
               armor_set%lowrank=.false.
               if(if_info)write(io_unit,"(A)")"   High Rank"
            endif
         case ("blade or gunner")
            if((arg(1:1).eq."g").or.(arg(1:1).eq."G"))then
               armor_set%blade_or_gunner="G"
               if(if_info)write(io_unit,"(A)")"   Gunner Set"
            else  ! default
               armor_set%blade_or_gunner="B"
               if(if_info)write(io_unit,"(A)")"   Blade Master"
            endif
         case ("set name")
            if(arg.eq."")then
               armor_set%set_name="Unnamed Set"
            else
               armor_set%set_name=arg
            endif
            if(if_info)write(io_unit,"(2A)")"   Set Name : ", &
               trim(armor_set%set_name)
         case ("head part")
            read(arg,*,iostat=error)id
            if(error.lt.0)then
               if(if_info)write(io_unit,"(A)")"   Error in Input File!"
               stop
            endif
            if(id.gt.0)then
               if(if_info)write(io_unit,"(6A)")"   Head Piece  : ", &
                  trim(armor_list(id,1)%armor_name),  &
                  "  ",("o",i=1,armor_list(id,1)%num_slot)
               armor_set%armor_id(1)=id
            endif
         case ("chest part")
            read(arg,*,iostat=error)id
            if(error.lt.0)then
               if(if_info)write(io_unit,"(A)")"   Error in Input File!"
               stop
            endif
            if(id.gt.0)then
               if(if_info)write(io_unit,"(6A)")"   Chest Piece : ", &
                  trim(armor_list(id,2)%armor_name),  &
                  "  ",("o",i=1,armor_list(id,2)%num_slot)
               armor_set%armor_id(2)=id
            endif
         case ("arm part")
            read(arg,*,iostat=error)id
            if(error.lt.0)then
               if(if_info)write(io_unit,"(A)")"   Error in Input File!"
               stop
            endif
            if(id.gt.0)then
               if(if_info)write(io_unit,"(6A)")"   Arm Piece   : ", &
                  trim(armor_list(id,3)%armor_name),  &
                  "  ",("o",i=1,armor_list(id,3)%num_slot)
               armor_set%armor_id(3)=id
            endif
         case ("waist part")
            read(arg,*,iostat=error)id
            if(error.lt.0)then
               if(if_info)write(io_unit,"(A)")"   Error in Input File!"
               stop
            endif
            if(id.gt.0)then
               if(if_info)write(io_unit,"(6A)")"   Waist Piece:  ", &
                  trim(armor_list(id,4)%armor_name),  &
                  "  ",("o",i=1,armor_list(id,4)%num_slot)
               armor_set%armor_id(4)=id
            endif
         case ("leg part")
            read(arg,*,iostat=error)id
            if(error.lt.0)then
               if(if_info)write(io_unit,"(A)")"   Error in Input File!"
               stop
            endif
            if(id.gt.0)then
               if(if_info)write(io_unit,"(6A)")"   Leg Piece   : ", &
                  trim(armor_list(id,5)%armor_name),  &
                  "  ",("o",i=1,armor_list(id,5)%num_slot)
               armor_set%armor_id(5)=id
            endif
         case ("head jewel")
            call extract_numbers(arg,3,nnum,nums)
            if(if_info)write(io_unit,"(7A)")"   Head Jewel  : ", &
               (trim(jewel_list(nums(i))%jewel_name),"  ",i=1,nnum)
            armor_set%jewel_id(:,1)=nums
         case ("chest jewel")
            call extract_numbers(arg,3,nnum,nums)
            if(if_info)write(io_unit,"(7A)")"   Chest Jewel  : ", &
               (trim(jewel_list(nums(i))%jewel_name),"  ",i=1,nnum)
            armor_set%jewel_id(:,2)=nums
         case ("arm jewel")
            call extract_numbers(arg,3,nnum,nums)
            if(if_info)write(io_unit,"(7A)")"   Arm Jewel   : ", &
               (trim(jewel_list(nums(i))%jewel_name),"  ",i=1,nnum)
            armor_set%jewel_id(:,3)=nums
         case ("waist jewel")
            call extract_numbers(arg,3,nnum,nums)
            if(if_info)write(io_unit,"(7A)")"   Waist Jewel : ", &
               (trim(jewel_list(nums(i))%jewel_name),"  ",i=1,nnum)
            armor_set%jewel_id(:,4)=nums
         case ("leg jewel")
            call extract_numbers(arg,3,nnum,nums)
            if(if_info)write(io_unit,"(7A)")"   Leg Jewel   : ", &
               (trim(jewel_list(nums(i))%jewel_name),"  ",i=1,nnum)
            armor_set%jewel_id(:,5)=nums
         case ("weapon jewel")
            call extract_numbers(arg,3,nnum,nums)
            if(if_info)write(io_unit,"(7A)")"   Weapon Jewel: ", &
               (trim(jewel_list(nums(i))%jewel_name),"  ",i=1,nnum)
            armor_set%jewel_id(:,6)=nums
         case ("charm")
            read(arg,*,iostat=error)id
            if(error.lt.0)then
               if(if_info)write(io_unit,"(A)")"   Error in Input File!"
               stop
            endif
            armor_set%charm_id=id
         case ("charm skill")
            call extract_numbers(arg,2,nnum,nums(1:2))
            armor_set%charm_skill_id(:)=nums(1:2)
         case ("charm jewel")
            call extract_numbers(arg,3,nnum,nums)
            if(if_info)write(io_unit,"(7A)")"   Charm Jewel : ", &
               (trim(jewel_list(nums(i))%jewel_name),"  ",i=1,nnum)
            armor_set%jewel_id(:,7)=nums
         end select

      enddo
      close(1)

      call write_charm(armor_set%charm_id,armor_set%charm_skill_id,line)
      if(if_info)write(io_unit,"(2A)")"   Charm       : ",trim(line)

   end subroutine mhag_input_cal

   subroutine read_data
      call check_file
      call read_file
   end subroutine read_data

   subroutine check_file ! check if data files exist
      logical :: fexist
      logical :: if_stop

      if(if_info)write(io_unit,"(A)")"Checking Data...."

      if_stop=.false.
      inquire(file=trim(data_dir)//trim(file_skill),exist=fexist)
      if(.not.fexist)then
         if(if_info)write(io_unit,"(A)")"Error!,Cannot find Skill Data!"
         if_stop=.true.
      endif
      inquire(file=trim(data_dir)//trim(file_armor),exist=fexist)
      if(.not.fexist)then
         if(if_info)write(io_unit,"(A)")"Error!,Cannot find Armor Data!"
         if_stop=.true.
      endif
      inquire(file=trim(data_dir)//trim(file_jewel),exist=fexist)
      if(.not.fexist)then
         if(if_info)write(io_unit,"(A)")"Error!,Cannot find Jewel Data!"
         if_stop=.true.
      endif
      inquire(file=trim(data_dir)//trim(file_charm),exist=fexist)
      if(.not.fexist)then
         if(if_info)write(io_unit,"(A)")"Error!,Cannot find Charm Data!"
         if_stop=.true.
      endif
      if(if_stop)then
         if(if_info)write(io_unit,"(A)")"   Please Check!"
         stop
      else
         if(if_info)write(io_unit,"(A)")"   Data Found!"
      endif

   end subroutine check_file

   subroutine read_file

   if(if_info)write(io_unit,"(A)")"Reading Data..."

      call read_file_skill
      call read_file_jewel
      call read_file_armor
      call read_file_charm

   end subroutine read_file

   subroutine read_file_skill 
      integer :: eof,i,j,k
      character(len=255) :: line

      open(unit=1,file=trim(data_dir)//trim(file_skill),status='old')
      num_skill=0
      num_effect=0
      do
         read(1,"(A255)",iostat=eof)line
         if(eof.lt.0)exit
         if(line(1:1).eq."#")cycle
         num_skill=num_skill+1
      enddo
      rewind(1)
      if(if_info)write(io_unit,"(A,I4)")"   Number of skills: ", &
         num_skill

      allocate(skill_list(num_skill))
      call init_skill
      i=0
      do
         read(1,"(A255)",iostat=eof)line
         if(eof.lt.0)exit
         if(line(1:1).eq."#")cycle
         i=i+1

         call read_skill_line(line,skill_list(i))
         num_effect=num_effect+skill_list(i)%num_effect
!        print *,i,num_effect,skill_list(i)%skill_name

      enddo
      close(1)
      if(if_info)write(io_unit,"(A,I4)")"   Number of Skill Effects: ", &
         num_effect

      ! Generate effect list table

      allocate(effect_list(num_effect))
      k=0
      do i=1,num_skill
         do j=1,skill_list(i)%num_effect
            k=k+1
            effect_list(k)=skill_list(i)%effect_name(j)
            skill_list(i)%effect_id(j)=k
         enddo
      enddo

!     do i=1,num_effect
!        print *,trim(effect_list(i))
!     enddo
!     stop

   end subroutine read_file_skill

   subroutine read_file_jewel
      integer :: eof,i,j,k
      character(len=255) :: line

      open(unit=1,file=trim(data_dir)//trim(file_jewel),status='old')
      num_jewel=0
      do
         read(1,"(A255)",iostat=eof)line
         if(eof.lt.0)exit
         if(line(1:1).eq."#")cycle
         num_jewel=num_jewel+1
      enddo
      rewind(1)
      if(if_info)write(io_unit,"(A,I4)")"   Number of Jewels: ", &
         num_jewel

      allocate(jewel_list(num_jewel))
      call init_jewel
      i=0
      do
         read(1,"(A255)",iostat=eof)line
         if(eof.lt.0)exit
         if(line(1:1).eq."#")cycle
         i=i+1

         call read_jewel_line(line,jewel_list(i))
      enddo
      close(1)

!     do i=1,num_jewel
!        print "(6I5)",i,jewel_list(i)%num_skill,  &
!           (jewel_list(i)%skill_id(j),jewel_list(i)%skill_point(j), &
!           j=1,jewel_list(i)%num_skill)
!     enddo
!     stop

   end subroutine read_file_jewel

   subroutine read_file_armor
      integer :: eof,i,j,k,armor_type,i_5(5)
      character(len=255) :: line

      open(unit=1,file=trim(data_dir)//trim(file_armor),status='old')
      num_armor=0
      do
         read(1,"(A255)",iostat=eof)line
         if(eof.lt.0)exit
         if(line(1:1).eq."#")cycle
         call read_armor_type(line,armor_type)
         num_armor(armor_type)=num_armor(armor_type)+1
      enddo
      rewind(1)
      if(if_info)write(io_unit,"(A,5I4)") &
         "   Number of Armor Pierces: ",num_armor(:)
      
      max_armor=0
      do i=1,5
         max_armor=max(max_armor,num_armor(i))
      enddo
!     print *,max_armor

      allocate(armor_list(max_armor,5))
      call init_armor
      i_5(:)=0
      do
         read(1,"(A255)",iostat=eof)line
         if(eof.lt.0)exit
         if(line(1:1).eq."#")cycle
         call read_armor_type(line,armor_type)
         i_5(armor_type)=i_5(armor_type)+1
!         print *,trim(line)

         call read_armor_line(line,armor_list(i_5(armor_type), &
            armor_type))
      enddo
      close(1)

   end subroutine read_file_armor

   subroutine read_file_charm
      integer :: eof,i
      character(len=255) :: line

      open(unit=1,file=trim(data_dir)//trim(file_charm),status='old')
      num_charm=0
      do
         read(1,"(A255)",iostat=eof)line
         if(eof.lt.0)exit
         if(line(1:1).eq."#")cycle
         num_charm=num_charm+1
      enddo
      rewind(1)
      if(if_info)write(io_unit,"(A,I4)")"   Number of Charms: ", &
         num_charm
      
      allocate(charm_list(num_charm))
      call init_charm
      i=0
      do
         read(1,"(A255)",iostat=eof)line
         if(eof.lt.0)exit
         if(line(1:1).eq."#")cycle
         i=i+1

         call read_charm_line(line,charm_list(i))
      enddo
      close(1)

   end subroutine read_file_charm

   subroutine read_skill_line(line,skill_entry) !read skill 
      character(len=255),intent(in) :: line
      type(skill_type),intent(inout) :: skill_entry

      integer :: pos,i
      logical :: if_end
      character(len=255) :: word

      ! read Skill Name
      pos=0
      call extract_word(line,pos,word,if_end)

      skill_entry%skill_name=trim(word)
      if(if_end)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Skill Data is incorrect!"
         stop
      endif

      ! read Skill Class (A/B/C)
      call extract_word(line,pos,word,if_end)

      if((len_trim(word).ne.1).or.if_end)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Skill Data is incorrect!"
         stop
      endif
      skill_entry%skill_class=trim(word)

      ! read Skill Effects, max 6 effects
      do i=1,6
         call extract_word(line,pos,word,if_end)
         if(if_end)then
            if(if_info)write(io_unit,"(A)") &
               "Error!, Skill Data is incorrect!"
            stop
         endif
         skill_entry%effect_name(i)=trim(word)
         call extract_word(line,pos,word,if_end)
         read(word,*)skill_entry%effect_trigger(i)
         if(if_end)then
            skill_entry%num_effect=i
            exit
         endif
      enddo

   end subroutine read_skill_line

   subroutine read_jewel_line(line,jewel_entry) !read jewel
      character(len=255),intent(in) :: line
      type(jewel_type),intent(inout) :: jewel_entry

      integer :: pos,i,id
      logical :: if_end
      character(len=255) :: word

      ! read Jewel Name
      pos=0
      call extract_word(line,pos,word,if_end)

      jewel_entry%jewel_name=trim(word)
      if(if_end)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Jewel Data is incorrect!"
         stop
      endif

      ! read rl/hr (L/H)
      call extract_word(line,pos,word,if_end)

      if(if_end)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Jewel Data is incorrect!"
         stop
      endif
      select case(trim(word))
      case ("L")
         jewel_entry%lowrank=.true.
      case ("H")
         jewel_entry%lowrank=.false.
      case default
         if(if_info)write(io_unit,"(A)") &
            "Error!, Jewel Data is incorrect!"
         stop
      end select

      ! read num of slots
      call extract_word(line,pos,word,if_end)
      read(word,*)i
      if((i.lt.1).or.(i.gt.3))then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Jewel Data is incorrect!"
         stop
      endif
      jewel_entry%num_slot=i


      ! read skill name/points, convert them to id
      do i=1,2
         call extract_word(line,pos,word,if_end)
         if(if_end)then
            if(if_info)write(io_unit,"(A)") &
               "Error!, Jewel Data is incorrect!"
            stop
         endif
!        print *,trim(word)
         call search_skill(word,id)
         jewel_entry%skill_id(i)=id

         call extract_word(line,pos,word,if_end)
         read(word,*)jewel_entry%skill_point(i)
         if(if_end)then
            jewel_entry%num_skill=i
            exit
         endif
      enddo

   end subroutine read_jewel_line

   subroutine read_armor_type(line,armor_type)
      character(len=255),intent(in) :: line
      integer,intent(out) :: armor_type

      integer :: pos,i,id
      logical :: if_end
      character(len=255) :: word

      ! Skip Armor Name
      pos=0
      call extract_word(line,pos,word,if_end)
      ! Skip B/G/A
      call extract_word(line,pos,word,if_end)
      ! Read Parts
      call extract_word(line,pos,word,if_end)

      select case (trim(word))
      case ("H")
         armor_type=1
      case ("C")
         armor_type=2
      case ("A")
         armor_type=3
      case ("W")
         armor_type=4
      case ("L")
         armor_type=5
      case default
         if(if_info)write(io_unit,"(A)") &
            "Error!, Armor Data is incorrect!"
         if(if_info)write(io_unit,*)trim(word)
         stop
      end select

   end subroutine read_armor_type

   subroutine read_armor_line(line,armor_entry)
      character(len=255),intent(in) :: line
      type(armor_type),intent(inout) :: armor_entry

      integer :: pos,i,id
      logical :: if_end
      character(len=255) :: word

      ! Read Armor Name
      pos=0
      call extract_word(line,pos,word,if_end)
      if(if_end)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Armor Data is incorrect!"
         stop
      endif
      armor_entry%armor_name=trim(word)
!     print*,trim(word)

      !Read  B/G/A
      call extract_word(line,pos,word,if_end)
      if(if_end)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Armor Data is incorrect!"
         stop
      endif
      if((trim(word).eq."B").or.(trim(word).eq."G").or.(trim(word).eq."A"))then
         armor_entry%blade_or_gunner=trim(word)
      else
         if(if_info)write(io_unit,"(A)") &
            "Error!, Armor Data is incorrect!"
         stop
      endif
!     print*,trim(word)

      ! Read Parts
      call extract_word(line,pos,word,if_end)
      if(if_end.or.len_trim(word).ne.1)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Armor Data is incorrect!"
         stop
      endif
      armor_entry%body_part=trim(word)
!     print*,trim(word)

      ! Read lr defense
      call extract_word(line,pos,word,if_end)
      if(if_end)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Armor Data is incorrect!"
         stop
      endif
      if(trim(word).eq."--")then
         armor_entry%defense(1)=0
         armor_entry%lowrank=.false.
      else
         read(word,*)armor_entry%defense(1)
         armor_entry%lowrank=.true.
      endif
      
      ! Read hr defense
      call extract_word(line,pos,word,if_end)
      if(if_end)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Armor Data is incorrect!"
         stop
      endif
      read(word,*)armor_entry%defense(2)
!     print *,armor_entry%defense(:)

      ! Read  # of slots
      call extract_word(line,pos,word,if_end)
      if(if_end)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Armor Data is incorrect!"
         stop
      endif
      read(word,*)armor_entry%num_slot
!     print*,trim(word)

      ! Read Resist
      do i=1,5
         call extract_word(line,pos,word,if_end)
         read(word,*)armor_entry%resist(i)
         if(if_end)then
            if(i.eq.5)return
            if(if_info)write(io_unit,"(A)") &
               "Error!, Armor Data is incorrect!"
            stop
         endif
      enddo
!     print *,armor_entry%resist(:)

      ! read skill name/points, convert them to id
      do i=1,6   ! max possible skills, <=5)
         call extract_word(line,pos,word,if_end)
!        print *,trim(word)
         if(trim(word).eq."Torso Up")then
            armor_entry%num_skill=1
            armor_entry%skill_id(1)=-1
            exit
         endif

         if(if_end)then
            if(if_info)write(io_unit,"(A)") &
               "Error!, Armor Data is incorrect!"
            stop
         endif
         call search_skill(word,id)
         armor_entry%skill_id(i)=id

         call extract_word(line,pos,word,if_end)
         read(word,*)armor_entry%skill_point(i)
         if(if_end)then
            armor_entry%num_skill=i
            exit
         endif
      enddo

   end subroutine read_armor_line

   subroutine read_charm_line(line,charm_entry) !read charm
      character(len=255),intent(in) :: line
      type(charm_type),intent(inout) :: charm_entry

      integer :: pos,i,id
      logical :: if_end
      character(len=255) :: word

      ! read charm_class
      pos=0
      call extract_word(line,pos,word,if_end)
      read(word,*)i
      if(if_end.or.(i.lt.1).or.(i.gt.4))then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Charm Data is incorrect!"
         stop
      endif
      charm_entry%charm_class=i

      ! read number of slots
      call extract_word(line,pos,word,if_end)
      read(word,*)i
      if(if_end.or.(i.lt.0).or.(i.gt.3))then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Charm Data is incorrect!"
         stop
      endif
      charm_entry%num_slot=i

      ! read rl/hr (L/H)
      call extract_word(line,pos,word,if_end)

      if(if_end)then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Jewel Data is incorrect!"
         stop
      endif

      select case(trim(word))
      case ("L")
         charm_entry%lowrank=.true.
      case ("H")
         charm_entry%lowrank=.false.
      case default
         if(if_info)write(io_unit,"(A)") &
            "Error!, Jewel Data is incorrect!"
         stop
      end select

      ! read percentage point
      call extract_word(line,pos,word,if_end)
      read(word,*)i
      if((i.lt.0).or.(i.gt.100))then
         if(if_info)write(io_unit,"(A)") &
            "Error!, Charm Data is incorrect!"
         stop
      endif
      charm_entry%percentage=i
      if(if_end)then
         return
      endif

      ! read skill name/points, convert them to id
      do i=1,2
         call extract_word(line,pos,word,if_end)
         if((len_trim(word).ne.1).or.if_end)then
            if(if_info)write(io_unit,"(A)") &
               "Error!, Jewel Data is incorrect!"
            stop
         endif
         charm_entry%skill_class=trim(word)

         call extract_word(line,pos,word,if_end)
         read(word,*)charm_entry%skill_point(i)
         if(if_end)then
            charm_entry%num_skill=i
            exit
         endif
      enddo

   end subroutine read_charm_line

   ! (universal) extract words based on ":"

   subroutine extract_word(line,pos,word,if_end)
      character(len=255),intent(in) :: line
      integer,intent(inout) :: pos
      character(len=255),intent(out) :: word
      logical,intent(out) :: if_end

      integer :: pos_new,pos_first,i

      pos_new=index(line(pos+1:),":")
      if(pos_new.eq.0)then
         if_end=.true.
         pos_new=255
      else
         if_end=.false.
         pos_new=pos_new+pos
      endif

      pos_first=0
      do i=pos+1,pos_new-1
         if(line(i:i).ne." ")then
            pos_first=i
            exit
         endif
      enddo
      if(pos_first.eq.0)then
         if(if_info)write(io_unit,"(A)") &
            "Error in Data Format!"
         stop
      endif
      word=trim(line(pos_first:pos_new-1))
      pos=pos_new

   end subroutine extract_word

   ! extract non-zero numbers from a line 

   subroutine extract_numbers(line,nmax,nnum,nums)
      character(len=255),intent(in) :: line
      integer,intent(in) :: nmax         !max possible numbers
      integer,intent(out) :: nnum,nums(nmax)

      integer :: eof,n
      integer :: pos,pos2

      nnum=0
      nums(:)=0
      pos=1
      do while (pos.le.len_trim(line))
         do while (line(pos:pos).eq." ")
            pos=pos+1
         enddo
         pos2=pos+index(line(pos:)," ")-1

!        print *,pos,pos2, line(pos:pos2-1)
         read(line(pos:pos2-1),*,iostat=eof)n
         if(eof.lt.0)then
            if(if_info)write(io_unit,"(A)")"   Error in Input File!"
            stop
         endif
         
         if(n.ne.0)then   ! skip number zero
            nnum=nnum+1
            if(nnum.gt.nmax)then
               if(if_info)write(io_unit,"(A)") &
                  "   Warning! Too many numbers! Skip"
               return
            else
               nums(nnum)=n
            endif
         endif
         pos=pos2
      enddo
!     print *,nnum
!     print *,nums

   end subroutine extract_numbers

   ! list initialization (''/0/true)

   subroutine init_skill
      integer :: i
      do i=1,num_skill
         skill_list(i)%skill_name=''
         skill_list(i)%skill_class=''
         skill_list(i)%num_effect=0
         skill_list(i)%effect_name(:)=''
         skill_list(i)%effect_id=0
         skill_list(i)%effect_trigger=0
      enddo
   end subroutine init_skill

   subroutine init_jewel
      integer :: i
      do i=1,num_jewel
         jewel_list(i)%jewel_name=''
         jewel_list(i)%lowrank=.true.
         jewel_list(i)%num_slot=0
         jewel_list(i)%num_skill=0
         jewel_list(i)%skill_id=0
         jewel_list(i)%skill_point=0
      enddo
   end subroutine init_jewel

   subroutine init_armor
      integer :: i,j
      do j=1,5
         do i=1,max_armor
            armor_list(i,j)%armor_name=''
            armor_list(i,j)%blade_or_gunner=''
            armor_list(i,j)%body_part=''
            armor_list(i,j)%lowrank=.true.
            armor_list(i,j)%defense(:)=0
            armor_list(i,j)%num_slot=0
            armor_list(i,j)%resist(:)=0
            armor_list(i,j)%num_skill=0
            armor_list(i,j)%skill_id=0
            armor_list(i,j)%skill_point=0
         enddo
      enddo
   end subroutine init_armor

   subroutine init_charm
      integer :: i
      do i=1,num_charm
         charm_list(i)%charm_class=0
         charm_list(i)%num_slot=0
         charm_list(i)%lowrank=.true.
         charm_list(i)%percentage=0
         charm_list(i)%num_skill=0
         charm_list(i)%skill_class=''
         charm_list(i)%skill_point=0
      enddo
   end subroutine init_charm

   ! search a skill name from the whole list, and return the id

   subroutine search_skill(skill_name,skill_id)
      character*255,intent(in) :: skill_name
      integer,intent(out) :: skill_id
      integer :: i

      skill_id=0
      do i=1,num_skill
         if(trim(skill_name).eq.trim(skill_list(i)%skill_name))then
            skill_id=i
            exit
         endif
      enddo
      if(skill_id.eq.0)then
         if(if_info)write(io_unit,"(A)")"Skill Names Incorrect!"
         if(if_info)write(io_unit,*)trim(skill_name)
         stop
      endif
   end subroutine search_skill

   ! data processing 
   ! 1. generate skill list for each class (A/B/C...)
   subroutine data_pre_proc
      integer :: i,j,class_id
      integer :: nums(10) !,percentages(10)

      num_skill_in_class(:)=0
      allocate(skill_in_class_list(num_skill,5))

   ! 1. generate skill list for each class (A/B/C...)
      do i=1,num_skill
         select case (skill_list(i)%skill_class)
         case ("A")
            class_id=1
         case ("B")
            class_id=2
         case ("C")
            class_id=3
         case ("D")    !reserved for mhf3,auto-guard
            class_id=4
         case default
            class_id=5
         end select
         num_skill_in_class(class_id)=num_skill_in_class(class_id)+1
         skill_in_class_list(num_skill_in_class(class_id),class_id)=i
      enddo

  ! 1. process charm percentage data
      nums(:)=0
!     percentages(:)=0
      do i=1,num_charm
         j=charm_list(i)%charm_class
         nums(j)=nums(j)+1
!        percentages(j)=percentages(j)+charm_list(i)%percentage
      enddo
!      do i=1,10
!         print *,i,nums(i),percentages(i)
!      enddo
!      stop

      ! take average number for imcomplete data
      do i=1,num_charm
         j=charm_list(i)%charm_class
         if(charm_list(i)%percentage.eq.0)then
            charm_list(i)%percentage=100/nums(j)
         endif
      enddo

   ! 3. show reference data

      if(method.eq.4)then
         call show_skill_list
         call show_jewel_list
         call show_armor_list
         call show_charm_list
         call show_effect_list
         call show_skill_in_class
         stop
      endif

   end subroutine data_pre_proc

   ! show reference data

   subroutine show_skill_list
      integer :: i

      open(unit=2,file='ref_skill',status='replace')
      do i=1,num_skill
         write(2,"(I5,4X,A)")i,skill_list(i)%skill_name
      enddo 
      close(2)

   end subroutine show_skill_list

   subroutine show_jewel_list
      integer :: i,j

      open(unit=2,file='ref_jewel',status='replace')
      do i=1,num_jewel
         write(2,"(I5,4X,A,2(4X,A,3X,I4))")i,jewel_list(i)%jewel_name, &
            (skill_list(jewel_list(i)%skill_id(j))%skill_name, &
            jewel_list(i)%skill_point(j),j=1,jewel_list(i)%num_skill)
      enddo 
      close(2)

   end subroutine show_jewel_list

   subroutine show_armor_list
      integer :: i,j,pos_5(5)
      logical :: add_line
      character(len=255) :: firstline(5),secondline(5)

      open(unit=2,file='ref_armor',status='replace')
      do i=1,max_armor
         pos_5(:)=0
         add_line=.false.
         do j=1,5
            pos_5(j)=index(armor_list(i,j)%armor_name,"/")
            if(pos_5(j).ne.0)then
               add_line=.true.
               firstline(j)=armor_list(i,j)%armor_name(1:pos_5(j)-1)
               secondline(j)=armor_list(i,j)%armor_name(pos_5(j)+1:)
            endif
         enddo
         if(.not. add_line)then
            write(2,"(I5,4X,5A25)")i,(armor_list(i,j)%armor_name,j=1,5)
         else
            do j=1,5
               if(pos_5(j).eq.0)then
                  firstline(j)=armor_list(i,j)%armor_name
                  secondline(j)=''
               endif
            enddo
            write(2,"(I5,4X,5A25)")i,(firstline(j),j=1,5)
            write(2,"(5X,4X,5A25)")(secondline(j),j=1,5)
         endif
      enddo 
      close(2)

   end subroutine show_armor_list

   subroutine show_charm_list
      integer :: i,j

      open(unit=2,file="ref_charm",status="replace")
      do i=1,num_charm
         write(2,"(2I5,A5,I5,A5,I5)")i,charm_list(i)%num_slot,  &
            (charm_list(i)%skill_class(j),charm_list(i)%skill_point(j),&
            j=1,charm_list(i)%num_skill)
      enddo 
      close(2)

   end subroutine show_charm_list

   subroutine show_effect_list
      integer :: i

      open(unit=2,file="ref_effect",status="replace")
      do i=1,num_effect
         write(2,"(I5,4X,A)")i,effect_list(i)
      enddo 
      close(2)

   end subroutine show_effect_list

   subroutine show_skill_in_class
      integer :: i,j

      open(unit=2,file='ref_skill_in_class',status='replace')
      do i=1,5
         write(2,"(A,I3,A)")"Class",i,":"
         do j=1,num_skill_in_class(i)
            write(2,"(2I5,4X,A)")i,j,skill_list(skill_in_class_list(j,i))%skill_name
         enddo
         write(2,"(A)")""
      enddo
      close(2)

   end subroutine show_skill_in_class

   !convert set parameters to set type strucutre, for future interface
   subroutine set_converter(set_name,lowrank,blade_or_gunner,armor_id, &
      jewel_id,charm_id,charm_skill_id,armor_set)
      ! inputs
      character(len=255),intent(in) :: set_name
      logical,intent(in) :: lowrank
      character(len=1),intent(in) :: blade_or_gunner
      integer,intent(in) :: armor_id(5)
      integer,intent(in) :: jewel_id(3,7)
      integer,intent(in) :: charm_id
      integer,intent(in) :: charm_skill_id(2)
      ! outputs
      type(set_type),intent(out) :: armor_set

      armor_set%lowrank=lowrank
      armor_set%blade_or_gunner=blade_or_gunner
      armor_set%armor_id(:)=armor_id(:)
      armor_set%jewel_id(:,:)=jewel_id(:,:)
      armor_set%charm_id=charm_id
      armor_set%charm_skill_id(:)=charm_skill_id(:)
      armor_set%defense=0
      armor_set%resist=0
      armor_set%num_skill=0
      armor_set%skill_id(:)=0
      armor_set%skill_point(:)=0
      armor_set%num_effect=0
      armor_set%num_torso=1
      armor_set%effect_id(:)=0
      armor_set%effect_skill_index(:)=0
      armor_set%rate=0
   end subroutine set_converter

   subroutine init_set_data(armor_set)
      type(set_type),intent(out) :: armor_set

      armor_set%set_name="Unnamed Set"
      armor_set%lowrank=.false.
      armor_set%blade_or_gunner=''
      armor_set%armor_id(:)=0
      armor_set%jewel_id(:,:)=0
      armor_set%charm_id=0
      armor_set%charm_skill_id(:)=0
      armor_set%defense=0
      armor_set%resist=0
      armor_set%num_skill=0
      armor_set%skill_id(:)=0
      armor_set%skill_point(:)=0
      armor_set%num_effect=0
      armor_set%num_torso=1
      armor_set%effect_id(:)=0
      armor_set%effect_skill_index(:)=0
      armor_set%rate=0
   end subroutine init_set_data
   
   ! save charm class info in a line
   subroutine write_charm_class(charm_id,line)
      integer,intent(in) :: charm_id
      character(len=255),intent(out) :: line
      integer :: i,j
      character(len=3) :: points(2)

      line=''
      if(charm_id.eq.0)return

      do i=1,charm_list(charm_id)%num_skill
         j=charm_list(charm_id)%skill_point(i)
         if(j.gt.0)then
            write(points(i),"(A,I2)")"+",j
         else
            write(points(i),"(I3)")j
         endif
      enddo
      
      write(line,"(11A)")(charm_list(charm_id)%skill_class(i), &
         " ",points(i),"  ",i=1,charm_list(charm_id)%num_skill),&
         ("o",i=1,charm_list(charm_id)%num_slot)

!     print *,trim(line)

   end subroutine write_charm_class

   ! save charm information in a line

   subroutine write_charm(charm_id,charm_skill_id,line)
      integer,intent(in) :: charm_id,charm_skill_id(2)
      character(len=255),intent(out) :: line
      integer :: i,j
      character(len=3) :: points(2)

      line=''
      if(charm_id.eq.0)return

      do i=1,charm_list(charm_id)%num_skill
         j=charm_list(charm_id)%skill_point(i)
         if(j.gt.0)then
            write(points(i),"(A,I2)")"+",j
         else
            write(points(i),"(I3)")j
         endif
      enddo
      
      write(line,"(12A)")(trim(skill_list(charm_skill_id(i))%skill_name), &
         " ",points(i),"  ",i=1,charm_list(charm_id)%num_skill),  &
         "  ",("o",i=1,charm_list(charm_id)%num_slot)

!     print *,trim(line)

   end subroutine write_charm

   ! charm info for outputs

   subroutine write_charm2(charm_id,charm_skill_id,line)
      integer,intent(in) :: charm_id,charm_skill_id(2)
      character(len=255),intent(out) :: line
      integer :: i,j
      character(len=3) :: points(2)

      line=''
      if(charm_id.eq.0)return

      do i=1,charm_list(charm_id)%num_skill
         j=charm_list(charm_id)%skill_point(i)
         if(j.gt.0)then
            write(points(i),"(A,I2)")"+",j
         else
            write(points(i),"(I3)")j
         endif
      enddo
      
      write(line,"(12A)")(trim(skill_list(charm_skill_id(i))%skill_name), &
         " ",points(i),"  ",i=1,charm_list(charm_id)%num_skill)
!     print *,trim(line)

   end subroutine write_charm2
!
   subroutine gen_jewel_name_short(jewel,line)
      type(jewel_type),intent(in) :: jewel
      character(len=255),intent(out) :: line
      integer :: i,j,pos
      character(len=3) :: points(2)
      character(len=20) :: jewel_short

      line=''

      do i=1,jewel%num_skill
         j=jewel%skill_point(i)
         if(j.gt.0)then
            write(points(i),"(A,I2)")"+",j
         else
            write(points(i),"(I3)")j
         endif
      enddo

      pos=index(jewel%jewel_name,"Jewel")
      jewel_short=jewel%jewel_name(1:pos-1)
      if(index(jewel%jewel_name,"+").ne.0)then
         jewel_short=trim(jewel_short)//"+"
      endif
      
      write(line,"(7A)")trim(jewel_short)," (", &
         trim(skill_list(jewel%skill_id(1))%skill_name), &
         ")  ",("o",i=1,jewel%num_slot)

!     print *,trim(line)

   end subroutine gen_jewel_name_short

   ! Main subroutine !!! armor skill calculator

   subroutine armor_calculator(armor_set)
      type(set_type),intent(inout) :: armor_set

      call check_armor_set(armor_set)
      call math_armor_set(armor_set)

   end subroutine armor_calculator

   ! Check if set data is consistent
   ! 1. high rank / low rank
   ! 2. blademaster / gunner
   ! 3. slots / jewels 
   ! 4. charm skills / skill class

   subroutine check_armor_set(armor_set)
      type(set_type),intent(in) :: armor_set

      integer :: i,j,nslots,id
      character(len=1) :: blade_or_gunner
      
      if(if_info)write(io_unit,"(A)") &
         "Checking if the Armor Setup is consistent ..."
      !low rank check
      if(armor_set%lowrank)then
         ! check armor pieces
         do i=1,5
            if(armor_set%armor_id(i).eq.0)cycle
            if(.not.armor_list(armor_set%armor_id(i),i)%lowrank)then
               if(if_info)write(io_unit,"(A)") &
                  "   Error! not low rank armor piece!"
               stop
            endif
         enddo
         ! check jewels
         do i=1,7
            do j=1,3
               if(armor_set%jewel_id(j,i).eq.0)cycle
               if(.not.jewel_list(armor_set%jewel_id(j,i))%lowrank)then
                  if(if_info)write(io_unit,"(A)") &
                     "   Error! not low rank jewel!"
                  stop
               endif
            enddo
         enddo
         ! check charm
         if(armor_set%charm_id.ne.0)then
            if(.not.charm_list(armor_set%charm_id)%lowrank)then
               if(if_info)write(io_unit,"(A)") &
                  "   Error! not low rank charm!"
               stop
            endif
         endif
      endif

      ! blade/bowgun check
      if(armor_set%blade_or_gunner.eq."B")then
         do i=1,5
            if(armor_set%armor_id(i).eq.0)cycle
            if(armor_list(armor_set%armor_id(i),i)%blade_or_gunner  &
               .eq."G")then
               if(if_info)write(io_unit,"(A)") &
                  "   Error! not for Blademaster!"
               stop
            endif
         enddo
      else
         do i=1,5
            if(armor_set%armor_id(i).eq.0)cycle
            if(armor_list(armor_set%armor_id(i),i)%blade_or_gunner  &
               .eq."B")then
               if(if_info)write(io_unit,"(A)") &
                  "   Error! not for Gunner!"
               stop
            endif
         enddo
      endif

      !slots check
      ! armor slots
      do i=1,5
         if(armor_set%armor_id(i).eq.0)cycle
         nslots=0
         do j=1,3
            if(armor_set%jewel_id(j,i).ne.0)then
               nslots=nslots+jewel_list(armor_set%jewel_id(j,i))%num_slot
            endif
         enddo
         if(nslots.gt.armor_list(armor_set%armor_id(i),i)%num_slot)then
            if(if_info)write(io_unit,"(A)") &
               "   Error! Jewels require too many slots!"
            stop
         endif
      enddo

      if(armor_set%charm_id.ne.0)then
      ! charm slots
         nslots=0
         do j=1,3
            if(armor_set%jewel_id(j,7).ne.0)then
               nslots=nslots+jewel_list(armor_set%jewel_id(j,7))%num_slot
            endif
         enddo
         if(nslots.gt.charm_list(armor_set%charm_id)%num_slot)then
            if(if_info)write(io_unit,"(A)") &
               "   Error! Jewels require too many slots!"
            stop
         endif
      ! charm skills
         do i=1,2
            id=armor_set%charm_skill_id(i)
            if(id.ne.0)then
               if(skill_list(id)%skill_class.ne. &
                  charm_list(armor_set%charm_id)%skill_class(i))then
                  if(if_info)write(io_unit,"(A)") &
                     "   Error! Charm skill Class incorrect!"
                  stop
               endif
               if((i.eq.2).and.(id.eq.armor_set%charm_skill_id(1)))then
                  if(if_info)write(io_unit,"(A)") &
                     "   Error! Charm skills should be different!"
                  stop
               endif
            endif
         enddo
      endif

      if(if_info)write(io_unit,"(A)")"   No Error Detected!"

   end subroutine check_armor_set

   !do all math on the armor set
      
   subroutine math_armor_set(armor_set)
      type(set_type),intent(inout) :: armor_set
      integer :: i,j
      integer :: id,defense,resist(5)

      if(if_info)write(io_unit,"(A)")"Calulating Armor Stats..."

      ! calculate defense
      defense=0
      if(armor_set%lowrank)then
         do i=1,5
            id=armor_set%armor_id(i)
            if(id.eq.0)cycle
            defense=defense+armor_list(id,i)%defense(1)
         enddo
      else
         do i=1,5
            id=armor_set%armor_id(i)
            if(id.eq.0)cycle
            defense=defense+armor_list(id,i)%defense(2)
         enddo
      endif
      armor_set%defense=defense
      if(if_info)write(io_unit,"(A,I5)") &
         "   Total Defense : ",armor_set%defense

      ! calculate element resistance
      resist(:)=0
      do i=1,5
         id=armor_set%armor_id(i)
         if(id.eq.0)cycle
         resist(:)=resist(:)+armor_list(id,i)%resist(:)
      enddo
      armor_set%resist(:)=resist(:)
      if(if_info)write(io_unit,"(A,5I5)") &
         "   Total Resist  : ",armor_set%resist(:)

      ! add skills
      call add_skills(armor_set)

      if(if_info)write(io_unit,"(A)")"   Skill List : "
      do i=1,armor_set%num_skill
         if(if_info)write(io_unit,"(3X,I5,5X,A10,5X,I5)") &
            i,skill_list(armor_set%skill_id(i)) &
            %skill_name,armor_set%skill_point(i)
      enddo

      ! check skill effects
      call check_effects(armor_set)

      if(if_info)write(io_unit,"(A)")"   Effect List : "
      do i=1,armor_set%num_effect
         if(if_info)write(io_unit,"(3X,I5,5X,A)") &
            i,effect_list(armor_set%effect_id(i))
      enddo

   end subroutine math_armor_set

   ! add skills, calculate stats

   subroutine add_skills(armor_set)
      type(set_type),intent(inout) :: armor_set

      type(armor_type) :: armor
      type(jewel_type) :: jewel
      integer :: ntotal_skill,skill_id(100),skill_point(100)
      integer :: list_mapping(num_skill) !maps all available skills
      integer :: i,j,k,id,pos,num_torso

      ntotal_skill=0
      skill_id(:)=0
      skill_point(:)=0
      list_mapping(:)=0

      ! check torso up
      num_torso=1
      do i=1,5
         if((i.eq.2).or.(armor_set%armor_id(i).eq.0))cycle
         armor=armor_list(armor_set%armor_id(i),i)
         if((armor%num_skill.eq.1).and.(armor%skill_id(1).eq.-1))then
            num_torso=num_torso+1
         endif
      enddo
      armor_set%num_torso=num_torso

      ! add armor pierces
      do i=1,5
         if(armor_set%armor_id(i).eq.0)cycle
         armor=armor_list(armor_set%armor_id(i),i)
         do j=1,armor%num_skill
            id=armor%skill_id(j)
            if(id.le.0)cycle
            if(list_mapping(id).eq.0)then ! add skill in list
               ntotal_skill=ntotal_skill+1
               pos=ntotal_skill
               list_mapping(id)=pos
               skill_id(pos)=id
            else
               pos=list_mapping(id)
            endif
            if((i.eq.2).and.(num_torso.ne.1))then
               skill_point(pos)=skill_point(pos)+armor%skill_point(j) &
                  *num_torso
            else
               skill_point(pos)=skill_point(pos)+armor%skill_point(j)
            endif
         enddo
      enddo

      ! add jewels
      do i=1,7
         do j=1,3
            if(armor_set%jewel_id(j,i).eq.0)cycle
            jewel=jewel_list(armor_set%jewel_id(j,i))
            do k=1,jewel%num_skill
               id=jewel%skill_id(k)
               if(id.eq.0)cycle
               if(list_mapping(id).eq.0)then ! add skill in list
                  ntotal_skill=ntotal_skill+1
                  pos=ntotal_skill
                  list_mapping(id)=pos
                  skill_id(pos)=id
               else
                  pos=list_mapping(id)
               endif
               if((i.eq.2).and.(num_torso.ne.1))then
                  skill_point(pos)=skill_point(pos)+jewel%skill_point(k) &
                     *num_torso
               else
                  skill_point(pos)=skill_point(pos)+jewel%skill_point(k)
               endif
            enddo
         enddo
      enddo

      ! add charm skills
      do i=1,2
         id=armor_set%charm_skill_id(i)
         if(id.eq.0)cycle
         if(list_mapping(id).eq.0)then ! add skill in list
            ntotal_skill=ntotal_skill+1
            pos=ntotal_skill
            list_mapping(id)=pos
            skill_id(pos)=id
         else
            pos=list_mapping(id)
         endif
         skill_point(pos)=skill_point(pos)+ &
            charm_list(armor_set%charm_id)%skill_point(i)
      enddo

      armor_set%num_skill=ntotal_skill
      armor_set%skill_id(1:ntotal_skill)=skill_id(1:ntotal_skill)
      armor_set%skill_point(1:ntotal_skill)=skill_point(1:ntotal_skill)

!      print *,ntotal_skill
!      do i=1,ntotal_skill
!         print *,i,skill_list(skill_id(i))%skill_name, &
!            skill_point(i)
!      enddo
!      stop

   end subroutine add_skills

   subroutine check_effects(armor_set)
      type(set_type),intent(inout) :: armor_set

      integer :: i,j,skill_id,skill_point
      integer :: num_effect,effect_trigger

      do i=1,armor_set%num_skill
         skill_id=armor_set%skill_id(i)
         skill_point=armor_set%skill_point(i)
         num_effect=skill_list(skill_id)%num_effect

         if(skill_point.ge.10)then
            ! positive skills
            do j=num_effect,1,-1
               effect_trigger=skill_list(skill_id)%effect_trigger(j)
               if((effect_trigger.gt.0).and. &
                  (effect_trigger.le.skill_point))then
                  armor_set%num_effect=armor_set%num_effect+1
                  armor_set%effect_id(armor_set%num_effect)= &
                     skill_list(skill_id)%effect_id(j)
                  armor_set%effect_skill_index(armor_set%num_effect)=i
!                 print *,skill_list(skill_id)%effect_name(j)
                  exit
               endif
            enddo

         elseif(skill_point.le.-10)then
            ! negative skills
            do j=1,num_effect,1
               effect_trigger=skill_list(skill_id)%effect_trigger(j)
               if((effect_trigger.lt.0).and. &
                  (effect_trigger.ge.skill_point))then
                  armor_set%num_effect=armor_set%num_effect+1
                  armor_set%effect_id(armor_set%num_effect)= &
                     skill_list(skill_id)%effect_id(j)
                  armor_set%effect_skill_index(armor_set%num_effect)=i
!                 print *,skill_list(skill_id)%effect_name(j)
                  exit
               endif
            enddo

         else
            cycle
         endif

      enddo

   end subroutine check_effects

   ! output armor set

   subroutine armor_output(armor_set)
      type(set_type),intent(inout) :: armor_set
      if(outform.eq.2)then
         call armor_output_html(armor_set)
      else
         call armor_output_text(armor_set)
      endif
   end subroutine armor_output

   ! output armor set info in text format, sort effective skills
   subroutine armor_output_text(armor_set)
      type(set_type),intent(inout) :: armor_set

      integer :: i,j,k,rank_ind,pos
      integer :: armor_id,jewel_id,num_slot_weapon
      character(len=3) :: slots
      character(len=255) :: armor_name,title,title2,title5(5)
      character(len=10) :: jewel(3)
      character(len=20) :: effect_name
      integer :: skill_points(8,100),skill_ids(100),value5(5)
      character(len=3) :: point_word(8,100),word_torso(8)
      logical :: if_bonus
      integer :: bonus(6)
      character(len=6) :: bonus_title

      if(if_info)write(io_unit,"(A)")"Save Armor Set in TEXT Format..."
      write(3,"(80A)")("=",i=1,80) 

      ! Part 1 :  Setup information

      ! set name,low/high rank, melee/range
      if(armor_set%lowrank)then
         title="Low Rank"
      else
         title="High Rank"
      endif
      if(armor_set%blade_or_gunner.eq."B")then
         title2="Blademaster"
      else
         title2="Gunner"
      endif
      write(3,1000)armor_set%set_name,title,title2
      write(3,"(80A)")("-",i=1,80) 
!     title="Equipment"
!     title2="Jewels"
!     print 1000,title,"Slots",trim(title2)
!     print "(80A)",("-",i=1,80) 
      ! weapon
      call cal_weapon_slots(armor_set,num_slot_weapon)
      call gen_slot_word(num_slot_weapon,slots)
      jewel(:)=''
      k=0
      do j=1,3
         jewel_id=armor_set%jewel_id(j,6)
         if(jewel_id.eq.0)cycle
         k=k+1
         call jewel_name_cut(jewel_list(jewel_id)%jewel_name,jewel(k)) 
      enddo
      title="Weapon"
      write(3,1001)title,slots,jewel(:)
      !armor
      do i=1,5
         armor_id=armor_set%armor_id(i)
         if(armor_id.eq.0)then   ! emptry armor piece
            write(3,1001)empty_name
            cycle
         endif
         armor_name=armor_list(armor_id,i)%armor_name
         call gen_slot_word(armor_list(armor_id,i)%num_slot,slots) 
         jewel(:)=''
         k=0
         do j=1,3
            jewel_id=armor_set%jewel_id(j,i)
            if(jewel_id.eq.0)cycle
            k=k+1
            call jewel_name_cut(jewel_list(jewel_id)%jewel_name,jewel(k)) 
         enddo
         write(3,1001)armor_name,slots,jewel(:)
      enddo
      !charm
      call write_charm2(armor_set%charm_id,armor_set%charm_skill_id(:),title)
      title="CHARM: "//trim(title)
      if(armor_set%charm_id.ne.0)then
         call gen_slot_word(charm_list(armor_set%charm_id)%num_slot,slots) 
      else
         slots=''
      endif
      jewel(:)=''
      k=0
      do j=1,3
         jewel_id=armor_set%jewel_id(j,7)
         if(jewel_id.eq.0)cycle
         k=k+1
         call jewel_name_cut(jewel_list(jewel_id)%jewel_name,jewel(k)) 
      enddo
      write(3,1001)title,slots,jewel(:)
      write(3,"(80A)")("-",i=1,80) 

      ! Part 2 : Table (defense,elements,skills)
      write(3,1002)"WEP","HEA","CHE","ARM","WAI","LEG","CHA","TOT"
!     print "(80A)",("-",i=1,80) 

      ! check possible skill bonus
      call check_skill_bonus(armor_set,if_bonus,bonus)

      !defense
      if(armor_set%lowrank)then
         rank_ind=1   ! low rank index
      else
         rank_ind=2   ! high rank inddex
      endif
      title="Max Defense"
      do i=1,5
         if(armor_set%armor_id(i).eq.0)then
            value5(i)=0
         else
            value5(i)=armor_list(armor_set%armor_id(i),i)%defense(rank_ind)
         endif
      enddo
      bonus_title=""
      if(if_bonus.and.(bonus(6).ne.0))then
         armor_set%defense=armor_set%defense+bonus(6)
         if(bonus(6).gt.0)then
            bonus_title="<up>"
         else
            bonus_title="<down>"
         endif
      endif
      write(3,1003)title,"---",value5(:),"---",armor_set%defense,bonus_title

      !resist
      title5(1)="Resist: Fire"
      title5(2)="        Water"
      title5(3)="        Ice"
      title5(4)="        Thunder"
      title5(5)="        Dragon"
      do j=1,5
         do i=1,5
            if(armor_set%armor_id(i).eq.0)then
               value5(i)=0
            else
               value5(i)=armor_list(armor_set%armor_id(i),i)%resist(j)
            endif
         enddo
         bonus_title=""
         if(if_bonus.and.(bonus(j).ne.0))then
            armor_set%resist(j)=armor_set%resist(j)+bonus(j)
            if(bonus(j).gt.0)then
               bonus_title="<up>"
            else
               bonus_title="<down>"
            endif
         endif
         write(3,1004)title5(j),"---",value5(:),"---",armor_set%resist(j),bonus_title
      enddo
      write(3,"(80A)")("-",i=1,80) 

      call gen_list_skill_point(armor_set,skill_points,skill_ids)
      call point_to_word(armor_set,skill_points,point_word)

      ! add Torso Up if available
      if(armor_set%num_torso.ne.1)then
         call gen_list_torso_up(armor_set,word_torso)
         title="Skills: Torso Up"
         write(3,1005)title,word_torso(:),''
      endif

      do j=1,armor_set%num_skill
         if(j.le.armor_set%num_effect)then
            effect_name=effect_list(armor_set%effect_id(j))
         else
            effect_name='---'
         endif
         if((j.eq.1).and.(armor_set%num_torso.eq.1))then
            title="Skills: "//skill_list(skill_ids(j))%skill_name
         else
            title="        "//skill_list(skill_ids(j))%skill_name
         endif
!        write(3,1005)title,skill_points(:,j),effect_name
         write(3,1005)title,point_word(:,j),effect_name
      enddo

      write(3,"(80A)")("=",i=1,80) 
1000  format(A40,2A12)
!1000  format(A38,A5,3X,A)
1001  format(A40,A3,3X,3(A,X))
1002  format(20X,8(A3,1X))
1003  format(A20,A3,1X,5(I3,1X),A3,1X,I3,1X,A)
1004  format(A20,A3,1X,5(I3,1X),A3,1X,I3,1X,A)
!1005  format(A20,8(I3,1X),A)
1005  format(A20,8(A3,1X),A)

      if(if_info)write(io_unit,"(A)")"Armor Set Saved!"

   end subroutine armor_output_text

   ! output armor set info in html format, sort effective skills
   subroutine armor_output_html(armor_set)
      type(set_type),intent(inout) :: armor_set

      integer :: i,j,k,rank_ind,pos
      integer :: armor_id,jewel_id,num_slot_weapon
      integer :: num_row
      character(len=3) :: slots
      character(len=255) :: armor_name,title,title2,title5(5)
      character(len=10) :: jewel(3)
      character(len=20) :: effect_name
      integer :: skill_points(8,100),skill_ids(100),value5(5)
      character(len=3) :: point_word(8,100),word_torso(8)
      logical :: if_neg,if_bonus
      integer :: bonus(6)

      if(if_info)write(io_unit,"(A)")"Save Armor Set in HTML Format..."

      num_row=15+armor_set%num_skill

      ! Part 1 :  Setup information

      ! set name,low/high rank, melee/range
      call gen_html_headline(armor_set)

      ! weapon
      call cal_weapon_slots(armor_set,num_slot_weapon)
      call gen_slot_word_short(num_slot_weapon,slots)
      jewel(:)=''
      k=0
      do j=1,3
         jewel_id=armor_set%jewel_id(j,6)
         if(jewel_id.eq.0)cycle
         k=k+1
         call jewel_name_cut(jewel_list(jewel_id)%jewel_name,jewel(k)) 
      enddo
      title="Weapon"
      call gen_html_weapon_line(title,slots,jewel)

      !armor
      do i=1,5
         armor_id=armor_set%armor_id(i)
         if(armor_id.eq.0)then   ! emptry armor piece
            slots=''
            jewel(:)=''
            armor_name=trim(empty_name)
            call gen_html_setup_line(armor_name,slots,jewel)
            cycle
         endif
         armor_name=armor_list(armor_id,i)%armor_name
         call gen_slot_word_short(armor_list(armor_id,i)%num_slot,slots) 
         jewel(:)=''
         k=0
         do j=1,3
            jewel_id=armor_set%jewel_id(j,i)
            if(jewel_id.eq.0)cycle
            k=k+1
            call jewel_name_cut(jewel_list(jewel_id)%jewel_name,jewel(k)) 
         enddo
         call gen_html_setup_line(armor_name,slots,jewel)
      enddo
      !charm
      call write_charm2(armor_set%charm_id,armor_set%charm_skill_id(:),title)
      title="CHARM: "//trim(title)

      if(armor_set%charm_id.ne.0)then
         call gen_slot_word_short(charm_list(armor_set%charm_id)%num_slot,slots) 
      else
         slots=''
      endif
      jewel(:)=''
      k=0
      do j=1,3
         jewel_id=armor_set%jewel_id(j,7)
         if(jewel_id.eq.0)cycle
         k=k+1
         call jewel_name_cut(jewel_list(jewel_id)%jewel_name,jewel(k)) 
      enddo
      call gen_html_setup_line(title,slots,jewel)

      ! Part 2 : Table (defense,elements,skills)
      call gen_html_2nd_headline

      ! check possible skill bonus
      call check_skill_bonus(armor_set,if_bonus,bonus)

      !defense
      if(armor_set%lowrank)then
         rank_ind=1   ! low rank index
      else
         rank_ind=2   ! high rank inddex
      endif
      title="Max Defense"
      do i=1,5
         if(armor_set%armor_id(i).eq.0)then
            value5(i)=0
         else
            value5(i)=armor_list(armor_set%armor_id(i),i)%defense(rank_ind)
         endif
      enddo
!     write(3,1003)title,"---",value5(:),"---",armor_set%defense

      if(if_bonus.and.(bonus(6).ne.0))then
         armor_set%defense=armor_set%defense+bonus(6)
      endif
      call gen_html_defense_line(title,value5(:),armor_set%defense,if_bonus,bonus)

      !resist
      title5(1)="Fire"
      title5(2)="Water"
      title5(3)="Ice"
      title5(4)="Thunder"
      title5(5)="Dragon"
      do j=1,5
         do i=1,5
            if(armor_set%armor_id(i).eq.0)then
               value5(i)=0
            else
               value5(i)=armor_list(armor_set%armor_id(i),i)%resist(j)
            endif
         enddo

         if(if_bonus.and.(bonus(j).ne.0))then
            armor_set%resist(j)=armor_set%resist(j)+bonus(j)
         endif
         call gen_html_resist_line(j,value5(:),armor_set%resist(j))
      enddo

      call gen_list_skill_point(armor_set,skill_points,skill_ids)
      call point_to_word(armor_set,skill_points,point_word)

      ! add Torso Up if available
      if(armor_set%num_torso.ne.1)then
         call gen_list_torso_up_clean(armor_set,word_torso)
         call gen_html_torso_line(armor_set%num_skill,word_torso)
!        write(3,1005)title,word_torso(:),trim(empty_name)
      endif

      do j=1,armor_set%num_skill
         if_neg=.false.
         if(j.le.armor_set%num_effect)then
            effect_name=effect_list(armor_set%effect_id(j))
            if(skill_points(8,j).lt.0)then
               if_neg=.true.
            endif
         else
            effect_name=trim(empty_name)
         endif
         title=skill_list(skill_ids(j))%skill_name
         if((j.eq.1).and.(armor_set%num_torso.eq.1))then
            call gen_html_skill_line(armor_set%num_skill,j,title, &
               skill_points(:,j),effect_name,if_neg)
         else
            call gen_html_skill_line(armor_set%num_skill,j+1,title, &
               skill_points(:,j),effect_name,if_neg)
         endif
!        write(3,1005)title,skill_points(:,j),effect_name
      enddo

      ! if no skill portion, add 'empty' row to key table format
      if(armor_set%num_skill.eq.0)then
         call gen_html_skill_line_null
      endif

      write(3,"(A)")'</table>'  !finish table

      if(if_info)write(io_unit,"(A)")"Armor Set Saved!"

   end subroutine armor_output_html

   ! generate slot patterns 

   subroutine gen_slot_word(num_slot,str_slot)
      integer,intent(in) :: num_slot
      character(len=3),intent(out) :: str_slot

      select case(num_slot)
      case(0)
         str_slot="---"
      case(1)
         str_slot="O--"
      case(2)
         str_slot="OO-"
      case(3)
         str_slot="OOO"
      case default
         if(if_info)write(io_unit,"(A)")"Error!, incorrect # of slots!"
         stop
      end select

   end subroutine gen_slot_word

   subroutine gen_slot_word_short(num_slot,str_slot)
      integer,intent(in) :: num_slot
      character(len=3),intent(out) :: str_slot

      select case(num_slot)
      case(0)
         str_slot=""
      case(1)
         str_slot="O"
      case(2)
         str_slot="OO"
      case(3)
         str_slot="OOO"
      case default
         if(if_info)write(io_unit,"(A)")"Error!, incorrect # of slots!"
         stop
      end select

   end subroutine gen_slot_word_short

   ! calculate # of slots based on weapon jewels

   subroutine cal_weapon_slots(armor_set,num_slot_weapon)
      type(set_type),intent(in) :: armor_set
      integer,intent(out) :: num_slot_weapon
      integer :: i

      num_slot_weapon=0
      do i=1,3
         if(armor_set%jewel_id(i,6).ne.0)then
            num_slot_weapon=num_slot_weapon+1
         endif
      enddo

   end subroutine cal_weapon_slots

   ! make short version of jewel name

   subroutine jewel_name_cut(jewel_name,jewel_name_short)
      character(len=20),intent(in) :: jewel_name
      character(len=10),intent(out) :: jewel_name_short
      integer :: pos
      
      pos=index(jewel_name,"Jewel")
      jewel_name_short=jewel_name(:pos-1)
      if(index(jewel_name,"+").ne.0)then
         jewel_name_short=trim(jewel_name_short)//'+'
      endif

   end subroutine jewel_name_cut

   ! calculate skill points for each piece and sort skills

   subroutine gen_list_skill_point(armor_set,skill_points,skill_ids)
      type(set_type),intent(inout) :: armor_set
      integer,intent(out) :: skill_points(8,100),skill_ids(100)

      integer :: i,j,ind,num,mapping(100),effect_points(8),id
      logical :: active(100)
      integer :: effect_id(8),effect_skill_index(8)

      skill_points=0
      skill_ids=0
      active=.false.

!      num=0
!      do i=1,armor_set%num_effect
!         ind=armor_set%effect_skill_index(i)
!         num=num+1
!         skill_ids(num)=armor_set%skill_id(ind)
!         active(ind)=.true.
!         call extract_points(armor_set,skill_ids(num),skill_points(:,num))
!!        print *,num,skill_list(skill_ids(i))%skill_name
!      enddo

      call simple_sort(armor_set%num_skill,armor_set%skill_point,mapping)

      ! effect skills
      num=0
      effect_id=0
      effect_skill_index=0
      do i=1,armor_set%num_skill
         id=armor_set%skill_id(mapping(i))
         do j=1,armor_set%num_effect
            ind=armor_set%effect_skill_index(j)
            if(id.ne.armor_set%skill_id(ind))cycle
            num=num+1
            effect_id(num)=armor_set%effect_id(j)
            effect_skill_index(num)=ind
            skill_ids(num)=id
            active(ind)=.true.
            call extract_points(armor_set,skill_ids(num),skill_points(:,num))
         enddo
      enddo

      armor_set%effect_id=effect_id
      armor_set%effect_skill_index=effect_skill_index
      
      ! other skills
      do i=1,armor_set%num_skill
         if(active(mapping(i)))cycle
         num=num+1
         skill_ids(num)=armor_set%skill_id(mapping(i))
         call extract_points(armor_set,skill_ids(num),skill_points(:,num))
!        print *,num,skill_list(skill_ids(num))%skill_name
      enddo

   end subroutine gen_list_skill_point

   ! simple bubble sort, array ind returns sort index

   subroutine simple_sort(num,values,ind)
      integer,intent(in) :: num,values(:)
      integer,intent(out) :: ind(:)

      integer :: i,j,temp,jmax
      logical :: change

      do i=1,num
         ind(i)=i
      enddo

      jmax=num-1
      do i=1,num-1
         change=.false.
         do j=1,jmax
            if(values(ind(j)).le.values(ind(j+1)))then
               change=.true.
               temp=ind(j)
               ind(j)=ind(j+1)
               ind(j+1)=temp
            endif
         enddo
         if(.not.change)exit
      enddo

   end subroutine simple_sort

   ! extract skill point info for a skill

   subroutine extract_points(armor_set,id,point_list)
      type(set_type),intent(in) :: armor_set
      integer,intent(in) :: id
      integer,intent(out) :: point_list(8)

      integer :: i,j,k,armor_id,jewel_id,charm_id
      type(armor_type) :: armor
      type(jewel_type) :: jewel
      type(charm_type) :: charm

      point_list(:)=0
      !check armor 
      do i=1,5
         armor_id=armor_set%armor_id(i)
         if(armor_id.eq.0)cycle
         armor=armor_list(armor_id,i)
         do j=1,armor%num_skill
            if(armor%skill_id(j).ne.id)cycle
            if(i.eq.2)then
               point_list(i+1)=point_list(i+1)+ &
                  armor%skill_point(j)*armor_set%num_torso
            else
               point_list(i+1)=point_list(i+1)+armor%skill_point(j)
            endif
         enddo
      enddo
      !check jewel
      ! weapon jewel
      do i=1,3
         jewel_id=armor_set%jewel_id(i,6)
         if(jewel_id.eq.0)cycle
         jewel=jewel_list(jewel_id)
         do j=1,jewel%num_skill
            if(jewel%skill_id(j).ne.id)cycle
            point_list(1)=point_list(1)+jewel%skill_point(j)
         enddo
      enddo
      ! armor jewel
      do k=1,5
         do i=1,3
            jewel_id=armor_set%jewel_id(i,k)
            if(jewel_id.eq.0)cycle
            jewel=jewel_list(jewel_id)
            do j=1,jewel%num_skill
               if(jewel%skill_id(j).ne.id)cycle
               if(k.eq.2)then
                  point_list(k+1)=point_list(k+1)+ &
                     jewel%skill_point(j)*armor_set%num_torso
               else
                  point_list(k+1)=point_list(k+1)+jewel%skill_point(j)
               endif
            enddo
         enddo
      enddo
      ! charm jewel
      do i=1,3
         jewel_id=armor_set%jewel_id(i,7)
         if(jewel_id.eq.0)cycle
         jewel=jewel_list(jewel_id)
         do j=1,jewel%num_skill
            if(jewel%skill_id(j).ne.id)cycle
            point_list(7)=point_list(7)+jewel%skill_point(j)
         enddo
      enddo
      ! charm skills
      charm_id=armor_set%charm_id
      if(charm_id.ne.0)then
         charm=charm_list(charm_id)
         do i=1,charm%num_skill
            if(armor_set%charm_skill_id(i).ne.id)cycle
            point_list(7)=point_list(7)+charm%skill_point(i)
         enddo
      endif

      do i=1,armor_set%num_skill
         if(armor_set%skill_id(i).eq.id)then
            point_list(8)=armor_set%skill_point(i)
         endif
      enddo
!     print "(8I5)",point_list(:)

   end subroutine extract_points

   ! gen list for display Torso Up
   subroutine gen_list_torso_up(armor_set,word_torso)
      type(set_type),intent(in) :: armor_set
      character(len=3),intent(out) :: word_torso(8)

      integer :: i
      type(armor_type) :: armor

      word_torso="---"
      do i=1,5
         if(armor_set%armor_id(i).eq.0)cycle
         armor=armor_list(armor_set%armor_id(i),i)
         if(armor%num_skill.eq.1)then
            if(armor%skill_id(1).eq.-1)then  ! torso up
               word_torso(i+1)='  E'
            endif
         endif
      enddo

   end subroutine gen_list_torso_up

   subroutine gen_list_torso_up_clean(armor_set,word_torso)
      type(set_type),intent(in) :: armor_set
      character(len=3),intent(out) :: word_torso(8)

      integer :: i
      type(armor_type) :: armor

      word_torso=""
      do i=1,5
         if(armor_set%armor_id(i).eq.0)cycle
         armor=armor_list(armor_set%armor_id(i),i)
         if(armor%num_skill.eq.1)then
            if(armor%skill_id(1).eq.-1)then  ! torso up
               word_torso(i+1)='  E'
            endif
         endif
      enddo

   end subroutine gen_list_torso_up_clean

   !convert skill points from number to characters for output
   subroutine point_to_word(armor_set,skill_points,point_word)
      type(set_type),intent(in) :: armor_set
      integer,intent(in) :: skill_points(8,100)
      character(len=3),intent(out) :: point_word(8,100)

      integer :: i,j
      point_word="---"
      do i=1,armor_set%num_skill
         do j=1,8
            if(skill_points(j,i).ne.0)then
               write(point_word(j,i),"(I3)")skill_points(j,i)
            endif
         enddo
      enddo

   end subroutine point_to_word

   ! Batch Process of MHAG Calculator
   subroutine mhag_cal_batch
      type(set_type) :: armor_set
      character(len=255) :: line
      integer :: eof,num
      logical :: fexist

      if(if_info)write(io_unit,"(A)")"Method: MHAG Batch Calculator"
      if(if_info)write(io_unit,"(A)")"Reading Set Data..."

      inquire(file=trim(filein),exist=fexist)
      if(.not.fexist)then
         if(if_info)write(io_unit,"(A)")"Oops, input.dat cannot be found!"
         stop
      endif

      open(unit=1,file=trim(filein),status='old')
      num=0
      do
         read(1,'(A255)',iostat=eof)line
         if(eof.lt.0)exit
         if((line(1:1).eq."!").or.(line(1:1).eq."#"))cycle
         if(line.eq."")cycle

         num=num+1
         if(if_info)write(io_unit,"(A,I4)")"Set : ",num
         if(outform.eq.2)then
            if(num.eq.1)then
               write(3,"(A,I4,A)")"<p>Set ",num,'</p>'  !html form
            else
               write(3,"(A,I4,A)")'<p style="page-break-before:always">Set ',num,'</p>'  !html form
            endif
         else
            write(3,"(A,I4)")"Set : ",num  !text form
         endif

         call init_set_data(armor_set)
         call mhag_input_cal_simple(armor_set,line)

         call armor_calculator(armor_set)
         call armor_output(armor_set)

      enddo
      close(1)


   end subroutine mhag_cal_batch

   ! read mhag input data from a line (fixed format,used for batch)
   subroutine mhag_input_cal_simple(armor_set,line)
      type(set_type),intent(inout) :: armor_set
      character(len=255),intent(in) :: line

      integer :: pos
      logical :: if_end
      character(len=255) :: word
      character(len=1) :: rank

      pos=0
      call extract_word(line,pos,word,if_end)
      armor_set%set_name=trim(word)

      call extract_word(line,pos,word,if_end)
      read(word,*)rank,armor_set%blade_or_gunner, &
         armor_set%armor_id(:),armor_set%jewel_id(:,:), &
         armor_set%charm_id,armor_set%charm_skill_id(:)

      if(rank.eq."L")then
         armor_set%lowrank=.true.
      else
         armor_set%lowrank=.false.
      endif

   end subroutine mhag_input_cal_simple

   ! generate simple html head
   subroutine gen_html_head

      write(3,"(A)")'<!DOCTYPE HTML PUBLIC "-//W3C//&
         DTD HTML 4.0 Transitional//EN">'
      write(3,"(A)")'<HTML>'
      write(3,"(A)")'<HEAD>'
      write(3,"(A)")'<TITLE> Monster Hunter Armor Generator</TITLE>'
      write(3,"(A)")'<STYLE TYPE="text/css">'
      write(3,"(A)")'<!--'
      write(3,"(A)")'BODY'
      write(3,"(A)")'   {'
      write(3,"(A)")'   background-color:lightyellow;'
      write(3,"(A)")'   }'
      write(3,"(A)")'-->'
      write(3,"(A)")'</STYLE>'
      write(3,"(A)")'</HEAD>'
      write(3,"(A)")'<BODY>'

   end subroutine gen_html_head

   subroutine gen_html_end
      write(3,"(A)")'</BODY>'
      write(3,"(A)")'</HTML>'
   end subroutine gen_html_end

   subroutine gen_html_headline(armor_set)
      type(set_type),intent(in) :: armor_set

      character(len=255) :: title,title2,color

      if(armor_set%lowrank)then
         title="Low Rank"
         color="orange"
      else
         title="High Rank"
         color="orangered"
      endif
      if(armor_set%blade_or_gunner.eq."B")then
         title2="Blademaster"
      else
         title2="Gunner"
      endif
      write(3,"(A)")'<table border="1" cellpadding="2" width="650" rules="rows" frame="box">'
      write(3,"(3A)")'<tr><td colspan="8"><b>',trim(armor_set%set_name),'</b></td>'
      write(3,"(7A)")'<td colspan="3"><font color="',trim(color),'">', &
         trim(title),'</font></td><td>',trim(title2),'</td></tr>'

   end subroutine gen_html_headline

   ! start a new embedded table, add weapon line
   subroutine gen_html_weapon_line(title,slots,jewel)
      character(len=255),intent(in) :: title
      character(len=3),intent(in) :: slots
      character(len=10),intent(in) :: jewel(3)
      integer :: i

      write(3,"(A)")'<tr><td colspan="12"><table border="0" cellpadding="0" width="650" rules="none">'

      write(3,"(12A)")'<tr><td width=300>',trim(title),'</td><td align="center" width="30"><font size="2">', &
         trim(slots),'</font></td><td width="30"></td><td width=240>', &
         (trim(jewel(i)),' &nbsp; ',i=1,3),'</td></tr>'

   end subroutine gen_html_weapon_line  

   ! add armor/jewel setup line
   subroutine gen_html_setup_line(title,slots,jewel)
      character(len=255),intent(in) :: title
      character(len=3),intent(in) :: slots
      character(len=10),intent(in) :: jewel(3)
      integer :: i

      write(3,"(12A)")'<tr><td>',trim(title),'</td><td align="center"><font size="2">',&
         trim(slots),'</font></td><td></td><td>', &
         (trim(jewel(i)),' &nbsp; ',i=1,3),'</td></tr>'

   end subroutine gen_html_setup_line

   ! close embedded table, add 2nd headline
   subroutine gen_html_2nd_headline

      write(3,"(A)")'</table></td></tr>'

      write(3,"(A)")'<tr align="right" style="font-size:7pt"><td colspan="2"></td>'
      write(3,"(A)")'<td height="20">WEP</td><td>HEAD</td><td>CHEST</td><td>ARM</td><td>WAIST</td><td>LEG</td>'
      write(3,"(A)")'<td>CHM</td><td>TOT</td><td colspan="2"></td></tr>'

   end subroutine gen_html_2nd_headline

   ! add defense line, add bonus embedded table
   subroutine gen_html_defense_line(title,value5,defense,if_bonus,bonus)
      character(len=255),intent(in) :: title
      integer,intent(in) :: value5(5),defense
      logical,intent(in) :: if_bonus
      integer,intent(in) :: bonus(6)
      integer :: i

      write(3,"(3A)")'<tr align="right"><td colspan="2" align="left">',trim(title),'</td>'
      write(3,"(A,6(A,I3),A)")'<td>---',('</td><td>',value5(i),i=1,5), &
         '</td><td>---</td><td>',defense,'</td>'
      write(3,"(A)")'<td align="left" colspan="2" rowspan="6">'
      write(3,"(A)")'<table width="20" border="0" cellpadding="2" rules="none">'
      ! add bonus table
      if(if_bonus)then
         if(bonus(6).gt.0)then
            write(3,"(A)")'<tr align="center"><td>&nbsp;&uarr;</td></tr>'
         elseif(bonus(6).lt.0)then
            write(3,"(A)")'<tr align="center"><td>&nbsp;&darr;</td></tr>'
         else
            write(3,"(A)")'<tr><td>&nbsp</td></tr>'
         endif
         do i=1,5
            if(bonus(i).gt.0)then
               write(3,"(A)")'<tr align="center"><td>&nbsp;&uarr;</td></tr>'
            elseif(bonus(i).lt.0)then
               write(3,"(A)")'<tr align="center"><td>&nbsp;&darr;</td></tr>'
            else
               write(3,"(A)")'<tr><td>&nbsp;</td></tr>'
            endif
         enddo
      endif
      write(3,"(A)")'</table></td></tr>'

   end subroutine gen_html_defense_line

   ! various element line( including 1st line)
   subroutine gen_html_resist_line(ind,value5,resist)
      integer,intent(in) :: ind,value5(5),resist
      character(len=255):: title,color
      integer :: i

      select case (ind) !element type
      case (1) !fire
         write(3,"(A)")'<tr style="color:red" align="right"><td rowspan="5" valign="top" align="left">'
         write(3,"(A)")'<font color="black">Resist</font></td><td align="left"><font color="red">Fire</color></td>'
         write(3,"(A,6(A,I3),A)")'<td>---',('</td><td>',value5(i),i=1,5), &
            '</td><td>---</td><td>',resist,'</td></tr>'
      case default
         select case(ind)
         case (2) ! water
            title="Water"
            color="blue"
         case (3) ! Ice
            title="Ice"
            color="darkcyan"
         case (4) ! Thunder
            title="Thunder"
            color="orange"
         case (5) ! Dragon 
            title="Dragon"
            color="purple"
         end select
         write(3,"(5A)"),'<tr style="color:',trim(color),'" align="right"><td align="left">', &
            trim(title),'</td><td>---'
         write(3,"(6(A,I3),A)")('</td><td>',value5(i),i=1,5),'</td><td>---</td><td>',&
            resist,'</td></tr>'

      end select

   end subroutine gen_html_resist_line

   ! add skill line (including 1st line, format whole table)
   subroutine gen_html_skill_line(num_skill,ind,title, &
      skill_points,effect_name,if_neg)
      integer,intent(in) :: num_skill,ind,skill_points(8)
      character(len=255),intent(in) :: title
      character(len=20),intent(in) :: effect_name
      logical,intent(in) :: if_neg
      integer :: i
      character(len=6) :: arrow
      logical :: null_skill

      if(trim(effect_name).eq.trim(empty_name))then
         arrow="&nbsp;"
         null_skill=.true.
      else
         arrow="&rarr;"
         null_skill=.false.
      endif

      if(ind.eq.1)then  ! format table, add 1st line
         write(3,"(A,I3,A)")'<tr align="right"><td width="50" rowspan="',num_skill,'" valign="top" align="left">Skill</td>'
         write(3,"(2A)")'<td width="100" align="left">',trim(title)
         write(3,"(8(A,I3))")('</td><td width="30">',skill_points(i),i=1,8)
         if(if_neg)then !neagive skills
            write(3,"(6A)")'</td><td width="20" color=darkred>',arrow,'</td><td align="left">' &
               ,'<font color=darkred>',trim(effect_name),'</font></td></tr>'
         elseif(null_skill)then !inactive skills
            write(3,"(5A)")'</td><td width="20">',arrow,'</td> &
               <td align="left">',trim(effect_name),'</td></tr>'
         else !positive skills
            write(3,"(5A)")'</td><td width="20"><font color=darkblue>',arrow,'</font></td> &
               <td align="left"><font color=darkblue>',trim(effect_name),'</font></td></tr>'
         endif
      else
         write(3,"(2A,8(A,I3))")'<tr align="right"><td align="left">',trim(title), &
            ('</td><td>',skill_points(i),i=1,8)
         if(if_neg)then !neagive skills
            write(3,"(6A)")'</td><td><font color=darkred>',arrow,'</font></td><td align="left">' &
               ,'<font color=darkred>',trim(effect_name),'</font></td></tr>'
         elseif(null_skill)then !inactive skills
            write(3,"(5A)")'</td><td width="20">',arrow,'</td> &
               <td align="left">',trim(effect_name),'</td></tr>'
         else !positive skills
            write(3,"(5A)")'</td><td><font color=darkblue>',arrow,'</font></td> &
               <td align="left"><font color=darkblue>',trim(effect_name),'</font></td></tr>'
         endif
      endif

   end subroutine gen_html_skill_line

   ! add Torso Up line ,formate whole table
   subroutine gen_html_torso_line(num_skill,word_torso)
      integer,intent(in) :: num_skill
      character(len=3),intent(in) :: word_torso(8)
      integer :: i

      write(3,"(A,I3,A)")'<tr align="right"><td width="50" rowspan="',num_skill+1,'" valign="top" align="left">Skill</td>'
      write(3,"(A)")'<td width="100" align="left">Torso Up'
      write(3,"(8(A,A3,A))")('</td><td width="30"><font color="green">',word_torso(i),'</font>',i=1,8)
      write(3,"(A)")'</td><td width="20"></td><td align="left"></td></tr>'

   end subroutine gen_html_torso_line

   ! gen empty line in empty if there's no skill 
   subroutine gen_html_skill_line_null
      integer :: i
      
      write(3,"(A)")'<tr align="right"><td width="50" align="left">Skill</td>'
      write(3,"(2A)")'<td width="100" align="left">','No Skill'
      write(3,"(8(A,A3))")('</td><td width="30">','---',i=1,8)
      write(3,"(3A)")'</td><td width="20"></td><td align="left">',trim(empty_name),'</td></tr>'

   end subroutine gen_html_skill_line_null

   ! check stat bonus from activated skills
   subroutine check_skill_bonus(armor_set,if_bonus,bonus)
      type(set_type),intent(in) :: armor_set
      logical,intent(out) :: if_bonus
      integer,intent(out) :: bonus(6)    ! 1-5 resist,6, defense
      integer :: i,effect_id,pos,id
      character(len=20) :: effect_name

      if_bonus=.false.
      bonus=0
      do i=1,armor_set%num_effect
         effect_id=armor_set%effect_id(i)
         if(effect_id.eq.0)cycle
         effect_name=effect_list(effect_id)
         
         ! check defense skills
         if(index(effect_name,"Defense").ne.0)then
            if_bonus=.true.
            if(index(effect_name,"(L)").ne.0)then
               bonus(6)=20
            elseif(index(effect_name,"(M)").ne.0)then
               bonus(6)=15
            elseif(index(effect_name,"(S)").ne.0)then
               bonus(6)=10
            endif
            if(index(effect_name,"Down").ne.0)then
               bonus(6)=-bonus(6)
            endif
         endif
            
         ! check element resist skills
         id=0
         if(index(effect_name,"Fire Res").ne.0)then
            id=1
         elseif(index(effect_name,"Water Res").ne.0)then
            id=2
         elseif(index(effect_name,"Ice Res").ne.0)then
            id=3
         elseif(index(effect_name,"Thunder Res").ne.0)then
            id=4
         elseif(index(effect_name,"Dragon Res").ne.0)then
            id=5
         endif
         if(id.ne.0)then
            if_bonus=.true.
            if(index(effect_name,"-15").ne.0)then
               bonus(id)=-15
            elseif(index(effect_name,"-10").ne.0)then
               bonus(id)=-10
            elseif(index(effect_name,"+10").ne.0)then
               bonus(id)=10
            elseif(index(effect_name,"+15").ne.0)then
               bonus(id)=15
            elseif(index(effect_name,"+20").ne.0)then
               bonus(id)=20
            endif
         endif
      enddo

   end subroutine check_skill_bonus

end module mhag
