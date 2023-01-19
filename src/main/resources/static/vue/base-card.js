const BaseCard = {
    props:{
        title: String,
        content: {
            type: String,
            default: "",
            description: "card content"
        },
        buttonText: {
            type: String,
            default: "",
            description: "button text"
        }
    },
    template:`
    <div class="card" style="width: 18rem;margin: auto;">
        <img src="/images/kakao-imo.png" class="card-img-top" alt="...">
        <div class="card-body">
            <h5 class="card-title">{{ title }}</h5>
            <p class="card-text">{{ content }}</p>
            <button class="btn btn-primary" @click="handleClick">{{ buttonText }}</button>
        </div>
    </div>
    `
}