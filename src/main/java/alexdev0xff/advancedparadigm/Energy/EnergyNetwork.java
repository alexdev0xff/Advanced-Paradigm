package alexdev0xff.advancedparadigm.Energy;

import alexdev0xff.advancedparadigm.Blocks.ModBlocks;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EnergyNetwork {
    private static final int MAX_PIPE_NODES = 256;

    private EnergyNetwork() {
    }

    public static void pushFromPipeNetwork(Level level, BlockPos startPipePos, SimpleEnergyStorage from, long maxPerTick) {
        if (level.isClientSide()) {
            return;
        }
        if (from.amount <= 0 || maxPerTick <= 0) {
            return;
        }

        long remaining = Math.min(maxPerTick, from.amount);

        List<Endpoint> sinks = new ArrayList<>();
        List<Endpoint> others = new ArrayList<>();
        collectEndpoints(level, startPipePos, sinks, others);

        remaining = pushToEndpoints(level, from, remaining, sinks);
        pushToEndpoints(level, from, remaining, others);
    }

    private static long pushToEndpoints(Level level, SimpleEnergyStorage from, long remaining, List<Endpoint> endpoints) {
        for (Endpoint endpoint : endpoints) {
            if (remaining <= 0) {
                break;
            }

            EnergyStorage target = EnergyStorage.SIDED.find(level, endpoint.pos, endpoint.side);
            if (target == null || !target.supportsInsertion()) {
                continue;
            }

            try (Transaction tx = Transaction.openOuter()) {
                long inserted = target.insert(remaining, tx);
                if (inserted <= 0) {
                    continue;
                }

                long extracted = from.extract(inserted, tx);
                if (extracted == inserted) {
                    tx.commit();
                    remaining -= inserted;
                }
            }
        }
        return remaining;
    }

    private static void collectEndpoints(Level level, BlockPos startPipePos, List<Endpoint> sinks, List<Endpoint> others) {
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        queue.add(startPipePos);
        visited.add(startPipePos);

        // Avoid adding the same neighbor endpoint multiple times from different pipe nodes.
        Set<EndpointKey> endpointKeys = new HashSet<>();

        while (!queue.isEmpty() && visited.size() < MAX_PIPE_NODES) {
            BlockPos pipePos = queue.removeFirst();

            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = pipePos.relative(dir);
                BlockState neighborState = level.getBlockState(neighborPos);

                if (neighborState.getBlock() == ModBlocks.ENERGY_PIPE) {
                    if (visited.add(neighborPos)) {
                        queue.addLast(neighborPos);
                    }
                    continue;
                }

                // For non-pipe blocks: if they expose an EnergyStorage on this side, treat as endpoint.
                Direction sideToQuery = dir.getOpposite();
                EndpointKey key = new EndpointKey(neighborPos, sideToQuery);
                if (!endpointKeys.add(key)) {
                    continue;
                }

                EnergyStorage target = EnergyStorage.SIDED.find(level, neighborPos, sideToQuery);
                if (target == null || !target.supportsInsertion()) {
                    continue;
                }

                if (!target.supportsExtraction()) {
                    sinks.add(new Endpoint(neighborPos, sideToQuery));
                } else {
                    others.add(new Endpoint(neighborPos, sideToQuery));
                }
            }
        }
    }

    private record Endpoint(BlockPos pos, Direction side) {
    }

    private record EndpointKey(BlockPos pos, Direction side) {
    }
}

