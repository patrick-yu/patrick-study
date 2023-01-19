const SelectImage = {
	created() {
		console.log("created");
	},
	mounted() {
		console.log("mounted");
	},
    props: {
        buttonText: {
			type: String,
			default: "이미지 선택",
			description: "이미지 선택 버튼 이름"
		},
		name: {
			type: String,
			default: 'file',
			description: "File input name"
		}
    },
	methods: {
		selectImage() {
			const $root = $(this.$refs.root);
			var $input = $root.find("input[type=file]");
			$input.click();
		},
		deleteImage() {
			console.log("deleteImage");
			const $root = $(this.$refs.root);
			const $input = $root.find("input[type=file]");
			$input.val("");
			$root.find(".output img").remove();
		},
		showImage() {
			console.log("showImage");
			const $root = $(this.$refs.root);
			const $input = $root.find("input[type=file]");
			const $output = $root.find("div.output");
			let $img = $output.find("img");
			if ($input[0].files && $input[0].files[0]) {
				console.log("image1")
				if ($img.length > 0) {
					console.log("image1 remove");
					$img.remove();
				}
			    $img = $("<img>");
			    $output.append($img);

				var reader = new FileReader();
				reader.onload = function(e) {
				    $img.attr("src", e.target.result);
				};
				reader.readAsDataURL($input[0].files[0]);
			}
     	},
	},
    template:`
		<div class="img-upload" ref="root">
		    <div class="img-btn">
		        <button type="button" class="btn btn-primary" @click="selectImage">{{buttonText}}</button>
		        <input type="file" :name="name" @change="showImage" accept=".gif, .jpg, .png" style="display: none;">
		    </div>
		    <div class="output">
		        <button type="button" class="photo-del" id="btn-del-img-main-photo" @click="deleteImage">삭제</button>
		    </div>
		</div>
    `
}