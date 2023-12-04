.data
	new_line: .asciiz "\n"	# new line variable
	vary: .word 0
	varcount: .word 0
	varx: .word 0
.text
.globl main
main:
	li $v0, 2	# load the number $v0
	sw $v0, varx	# assign the variable
	la $t0, varx
	lw $v0, ($t0)	# load the variable into $v0
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 1	# load the number $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	add $v0, $t0, $v0	# add the two expressions
	sw $v0, vary	# assign the variable
	la $t0, varx
	lw $v0, ($t0)	# load the variable into $v0
	subu $sp, $sp, 4
	sw $v0, ($sp)
	la $t0, vary
	lw $v0, ($t0)	# load the variable into $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	add $v0, $t0, $v0	# add the two expressions
	sw $v0, varx	# assign the variable
	la $t0, varx
	lw $v0, ($t0)	# load the variable into $v0
	subu $sp, $sp, 4
	sw $v0, ($sp)
	la $t0, vary
	lw $v0, ($t0)	# load the variable into $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	mult $t0, $v0	# add the multiply expressions
	mflo $v0
	move $a0, $v0
	li $v0, 1	# print the expression
	syscall
	la $a0, new_line	# print a new line
	li $v0, 4
	syscall
	la $t0, varx
	lw $v0, ($t0)	# load the variable into $v0
	subu $sp, $sp, 4
	sw $v0, ($sp)
	la $t0, vary
	lw $v0, ($t0)	# load the variable into $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	ble $t0, $v0, endif1	# if $t0 is less than or equal to $v0
	la $t0, varx
	lw $v0, ($t0)	# load the variable into $v0
	move $a0, $v0
	li $v0, 1	# print the expression
	syscall
	la $a0, new_line	# print a new line
	li $v0, 4
	syscall
	la $t0, vary
	lw $v0, ($t0)	# load the variable into $v0
	move $a0, $v0
	li $v0, 1	# print the expression
	syscall
	la $a0, new_line	# print a new line
	li $v0, 4
	syscall
endif1:
	li $v0, 14	# load the number $v0
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 14	# load the number $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	bne $t0, $v0, endif2	# if $t0 and $v0 are not equal
	li $v0, 14	# load the number $v0
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 14	# load the number $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	beq $t0, $v0, endif3	# if $t0 and $v0 are equal
	li $v0, 3	# load the number $v0
	move $a0, $v0
	li $v0, 1	# print the expression
	syscall
	la $a0, new_line	# print a new line
	li $v0, 4
	syscall
endif3:
	li $v0, 14	# load the number $v0
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 14	# load the number $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	bgt $t0, $v0, endif4	# if $t0 is greater than $v0
	li $v0, 4	# load the number $v0
	move $a0, $v0
	li $v0, 1	# print the expression
	syscall
	la $a0, new_line	# print a new line
	li $v0, 4
	syscall
endif4:
endif2:
	li $v0, 15	# load the number $v0
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 14	# load the number $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	ble $t0, $v0, endif5	# if $t0 is less than or equal to $v0
	li $v0, 5	# load the number $v0
	move $a0, $v0
	li $v0, 1	# print the expression
	syscall
	la $a0, new_line	# print a new line
	li $v0, 4
	syscall
endif5:
	li $v0, 1	# load the number $v0
	sw $v0, varcount	# assign the variable
while1:
	la $t0, varcount
	lw $v0, ($t0)	# load the variable into $v0
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 15	# load the number $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	bgt $t0, $v0, term_while1	# if $t0 is greater than $v0
	la $t0, varcount
	lw $v0, ($t0)	# load the variable into $v0
	move $a0, $v0
	li $v0, 1	# print the expression
	syscall
	la $a0, new_line	# print a new line
	li $v0, 4
	syscall
	la $t0, varcount
	lw $v0, ($t0)	# load the variable into $v0
	subu $sp, $sp, 4
	sw $v0, ($sp)
	li $v0, 1	# load the number $v0
	lw $t0, ($sp)
	addu $sp, $sp, 4
	add $v0, $t0, $v0	# add the two expressions
	sw $v0, varcount	# assign the variable
	j while1	# repeat the while loop
term_while1:
program_exit:
	li $v0 10	# exit the program
	syscall

