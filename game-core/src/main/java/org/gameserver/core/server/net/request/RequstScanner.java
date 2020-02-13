package org.gameserver.core.server.net.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求扫描
 *
 * @Author: shengbao
 * @Date: 2020/3/10 16:26
 */
public class RequstScanner implements BeanFactoryPostProcessor {

    Logger logger = LoggerFactory.getLogger(RequstScanner.class);

    /**
     * 扫描路径
     **/
    List<String> locationPatterns = new ArrayList<>();

    /**
     * 请求扫描
     **/
    private static Map<Integer, Class<?>> classMap = new HashMap<>();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        findClasses();
    }

    public void setPackge(List<String> packages) {
        for (String p : packages) {
            String locationPattern = "classpath*:" + p.replaceAll("\\.", "/") + "/**/*.class";
            locationPatterns.add(locationPattern);
        }
    }

    private List<Class<?>> findClasses() {
        try {
            List<Class<?>> classList = new ArrayList<>();
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (String locationPattern : locationPatterns) {
                Resource[] rs = resourcePatternResolver.getResources(locationPattern);
                for (Resource r : rs) {
                    MetadataReader reader = metadataReaderFactory.getMetadataReader(r);
                    AnnotationMetadata annotationMD = reader.getAnnotationMetadata();
                    if (annotationMD.hasAnnotation(Request.class.getName())) {
                        ClassMetadata clazzMD = reader.getClassMetadata();
                        Class<?> clazz = Class.forName(clazzMD.getClassName());
                        Request annotation = clazz.getAnnotation(Request.class);
                        if (annotation != null) {
                            classMap.put(annotation.value(), clazz);
                            logger.info("注册请求:{} class:{}", annotation.value(), clazz);
                        }

                    }
                }
            }
            return classList;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static Class<?> getClass(int requestId) {
        Class<?> clazz = classMap.get(requestId);
        return clazz;
    }

}
