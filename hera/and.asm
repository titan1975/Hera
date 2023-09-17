.section .data
    andGate:
        .double 0.5, 0.5     # Weights
        .double 0.5          # Bias
    fmt:
        .string "%d%d\t%d\n"

.section .text
    .globl _start

_start:
    # Initialize stack pointer
    movl $0, %ebx          # Counter
    movl $andGate, %esi   # Load Neuron struct address into esi

loop_start:
    movsd (%esi), %xmm0   # Load weight1
    movsd 8(%esi), %xmm1  # Load weight2
    movsd 16(%esi), %xmm2 # Load bias
    movl %ebx, %eax       # Copy i to eax
    divl $2, %eax         # eax = i / 2
    movl %ebx, %ecx       # Copy i to ecx
    andl $1, %ecx         # ecx = i % 2
    movsd %xmm0, %xmm3    # Copy weight1 to xmm3
    mulsd (%esp), %xmm3   # xmm3 = xmm3 * x1
    movsd %xmm1, %xmm4    # Copy weight2 to xmm4
    mulsd 4(%esp), %xmm4  # xmm4 = xmm4 * x2
    addsd %xmm3, %xmm4    # xmm4 = xmm3 + xmm4
    addsd %xmm4, %xmm2    # xmm2 = xmm2 + xmm4
    ucomisd %xmm2, %xmm5  # Compare xmm2 with 0.0
    jae is_positive
    movl $0, %edx          # Set output to 0
    jmp print_result

is_positive:
    movl $1, %edx          # Set output to 1

print_result:
    push %edx
    push %ecx
    push %eax
    push $fmt
    call printf
    add $16, %esp         # Clean up the stack
    inc %ebx              # Increment i
    cmp $4, %ebx          # Compare i with 4
    jl loop_start         # If i < 4, repeat the loop

    # Exit the program
    movl $1, %eax          # syscall number for exit
    movl $0, %ebx          # exit status: 0
    int $0x80

