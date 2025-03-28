<script setup>
import NPlayer from 'nplayer';
import { onMounted, watch, ref, onBeforeUnmount } from 'vue';
import Danmaku from '@nplayer/danmaku';
import { getDanmakuByAnimeId, addDanmaku } from '../api/danmaku';

let player = null;
const currentUserId = parseInt(localStorage.getItem('userId'));



// 添加发送弹幕的方法
const sendDanmaku = async (danmakuData) => {
console.log('发送弹幕:', danmakuData);
  try {

    // 准备弹幕数据
    const danmakuData1 = {
      animeId: props.animeId,
      userId: currentUserId,
      content: danmakuData.text,
      time: danmakuData.time,
      color: danmakuData.color,
      type: danmakuData.type,
      fontSize: 25
    };
    console
    // 发送到后端
    const result = await addDanmaku(danmakuData1);

    if (result.code === 1) {
      // 添加到当前播放器的弹幕列表

      // 更新播放器的弹幕插件
      if (player) {
        player.removePlugin('danmaku');
        player.use(new Danmaku(danmakuOptions.value));
      }

      console.log('弹幕发送成功');
    }
  } catch (error) {
    console.error('发送弹幕失败:', error);
  }
};
// 初始化弹幕选项
const danmakuOptions = ref({
  items: []
});

// 定义 props
const props = defineProps({
  animeId: {
    type: Number,
    required: true
  },
  videoUrl: {
    type: String,
    required: true
  },
  autoplay: {
    type: Boolean,
    default: false
  },
});

// 加载弹幕数据
const loadDanmaku = async () => {
  try {
    const result = await getDanmakuByAnimeId(props.animeId);
    if (result.code === 1) {
      // 处理弹幕数据
      const danmakuItems = result.data.map(item => ({
        time: item.time,
        text: item.content,
        color: item.color || '#ffffff',
        type: item.type || 'scroll',
        fontSize: item.fontSize || 25,
        isMe: item.userId === currentUserId // 检查是否是当前用户发送的弹幕
      }));

      // 更新弹幕选项
      danmakuOptions.value.items = danmakuItems;

      // 如果播放器已初始化，重新设置弹幕
      if (player) {
        const danmakuPlugin = new Danmaku(danmakuOptions.value);
        player.use(danmakuPlugin);
      }
    }
  } catch (error) {
    console.error('加载弹幕失败:', error);
  }
};

const initPlayer = () => {
  //实例化一个播放器
  player = new NPlayer({
    src: props.videoUrl,
    autoplay: props.autoplay,
    controls: [
      ['play', 'volume', 'time', 'spacer', 'airplay', 'settings', 'web-fullscreen', 'fullscreen'],
      ['progress']
    ],
    plugins: [new Danmaku({
      ...danmakuOptions.value,
    })]
  });

  player.on('DanmakuSend', (danmakuData) => {
  console.log('用户发送的弹幕:', danmakuData);
  sendDanmaku(danmakuData);
  // danmakuData 结构示例：{ text: '弹幕内容', color: '#ff0000', time: 10 }
});
  //将实例对象挂在到一个容器
  player.mount(document.querySelector('.mv'));
};



// 清理函数
const cleanupPlayer = () => {
  if (player) {
    player.dispose();
    player = null;
  }
};

// 修改onMounted钩子
onMounted(async () => {
  await loadDanmaku();
  initPlayer();
});

// 添加onBeforeUnmount钩子
onBeforeUnmount(() => {
  cleanupPlayer();
});

// 修改监听逻辑
watch(() => props.videoUrl, async (newUrl) => {
  cleanupPlayer();
  if (newUrl) {
    await loadDanmaku();
    initPlayer();
  }
});

watch(() => props.animeId, async (newId, oldId) => {
  if (newId && newId !== oldId) {
    await loadDanmaku();
    if (player) {
      // 重新设置弹幕插件
      player.removePlugin('danmaku');
      player.use(new Danmaku(danmakuOptions.value));
    }
  }
}, { immediate: true });

</script>

<template>
  <div class="mv"></div>
</template>

<style scoped>
.mv {
  width: 100%;
  height: 100%;
}
</style>