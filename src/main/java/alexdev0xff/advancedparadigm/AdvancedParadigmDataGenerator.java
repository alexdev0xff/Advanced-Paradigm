package alexdev0xff.advancedparadigm;

import alexdev0xff.ModWorldGenerator;
import alexdev0xff.advancedparadigm.ModOre.ModConfiguredFeatures;
import alexdev0xff.advancedparadigm.ModOre.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class AdvancedParadigmDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // Добавляем провайдер, который создаст JSON-файлы
        pack.addProvider(ModWorldGenerator::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        // Регистрация конфигов (размер жилы, блоки)
        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        // Регистрация размещения (высота, шанс)
        registryBuilder.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
    }
}