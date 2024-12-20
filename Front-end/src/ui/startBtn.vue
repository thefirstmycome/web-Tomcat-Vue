<script setup>

</script>

<template>
    <button class="button">
        <p class="button__text">
            <span style="--index: 0;">S</span>
            <span style="--index: 1;">t</span>
            <span style="--index: 2;">a</span>
            <span style="--index: 3;">r</span>
            <span style="--index: 4;">t</span>
            <span style="--index: 5;"> </span>
            <span style="--index: 6;">I</span>
            <span style="--index: 7;">n</span>
            <span style="--index: 8;">t</span>
            <span style="--index: 9;">o</span>
            <span style="--index: 10;"> </span>
            <span style="--index: 11;">P</span>
            <span style="--index: 12;">r</span>
            <span style="--index: 13;">o</span>
            <span style="--index: 14;">j</span>
            <span style="--index: 15;">e</span>
            <span style="--index: 16;">c</span>
            <span style="--index: 17;">t</span>
        </p>

        <div class="button__circle">
            <svg viewBox="0 0 14 15" fill="none" xmlns="http://www.w3.org/2000/svg" class="button__icon" width="14">
                <path
                    d="M13.376 11.552l-.264-10.44-10.44-.24.024 2.28 6.96-.048L.2 12.56l1.488 1.488 9.432-9.432-.048 6.912 2.304.024z"
                    fill="currentColor"></path>
            </svg>

            <svg viewBox="0 0 14 15" fill="none" width="14" xmlns="http://www.w3.org/2000/svg"
                class="button__icon button__icon--copy">
                <path
                    d="M13.376 11.552l-.264-10.44-10.44-.24.024 2.28 6.96-.048L.2 12.56l1.488 1.488 9.432-9.432-.048 6.912 2.304.024z"
                    fill="currentColor"></path>
            </svg>
        </div>
    </button>

</template>

<style scoped>
/* From Uiverse.io by Creatlydev */
.button {
    cursor: pointer;
    border: none;
    background: #7808d0;
    color: #fff;
    width: 100px;
    height: 100px;
    border-radius: 50%;
    overflow: hidden;
    position: relative;
    display: grid;
    place-content: center;
    transition:
        background 300ms,
        transform 200ms;
    font-weight: 600;
}

.button__text {
    position: absolute;
    inset: 0;
    animation: text-rotation 8s linear infinite;

    >span {
        position: absolute;
        transform: rotate(calc(19deg * var(--index)));
        inset: -12px;
    }
}

.button__circle {
    position: relative;
    width: 40px;
    height: 40px;
    overflow: hidden;
    background: #fff;
    color: #7808d0;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.button__icon--copy {
    position: absolute;
    transform: translate(-150%, 150%);
}

.button:hover {
    background: #000;
    transform: scale(1.05);
}

.button:hover .button__icon {
    color: #000;
}

.button:hover .button__icon:first-child {
    transition: transform 0.3s ease-in-out;
    transform: translate(150%, -150%);
}

.button:hover .button__icon--copy {
    transition: transform 0.3s ease-in-out 0.1s;
    transform: translate(0);
}

@keyframes text-rotation {
    to {
        rotate: 360deg;
    }
}
</style>
