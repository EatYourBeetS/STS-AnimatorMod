package eatyourbeets.cards.effects.VestaElixirEffects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.animator.series.TenseiSlime.Vesta;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class VestaElixirEffects
{
    private final ArrayList<VestaElixirEffect> effects1 = new ArrayList<>();
    private final ArrayList<VestaElixirEffect> effects2 = new ArrayList<>();
    private final ArrayList<VestaElixirEffect> effects3 = new ArrayList<>();
    private final Vesta vesta;
    private final boolean upgraded;

    public Vesta_Elixir currentElixir;

    public VestaElixirEffects(Vesta vesta, boolean upgraded)
    {
        this.vesta = vesta;
        this.upgraded = upgraded;

        Random rng = EYBCard.rng;
        if (rng == null)
        {
            JUtils.LogInfo(VestaElixirEffects.class, "EYBCard.rng was null");
            rng = new Random();
        }

        final RandomizedList<VestaElixirEffect> effectPool = GenerateEffectPool(upgraded);

        effects1.add(effectPool.Retrieve(rng));
        effects1.add(effectPool.Retrieve(rng));
        effects1.add(effectPool.Retrieve(rng));

        effects2.add(effectPool.Retrieve(rng));
        effects2.add(effectPool.Retrieve(rng));
        effects2.add(effectPool.Retrieve(rng));

        effects3.add(effectPool.Retrieve(rng)); // new VestaElixirEffect_Exhaust()
        effects3.add(effectPool.Retrieve(rng)); // new VestaElixirEffect_Discard()
        effects3.add(new VestaElixirEffect_CompleteFaster());
    }

    public static void BeginCreateElixir(Vesta vesta, boolean upgraded)
    {
        final VestaElixirEffects effects = new VestaElixirEffects(vesta, upgraded);
        final ArrayList<AbstractCard> cards = new ArrayList<>();

        cards.add(Vesta_Elixir.DATA.MakeCopy(upgraded));

        effects.ChooseNextEffect(cards);
    }

    private void ChooseNextEffect(ArrayList<AbstractCard> cards)
    {
        currentElixir = JUtils.SafeCast(cards.get(0), Vesta_Elixir.class);
        if (currentElixir == null)
        {
            currentElixir = (Vesta_Elixir) Vesta_Elixir.DATA.MakeCopy(upgraded);
        }

        switch (currentElixir.effects.size())
        {
            case 0:
            {
                ChooseNextEffectAction(effects1);
                return;
            }

            case 1:
            {
                ChooseNextEffectAction(effects2);
                return;
            }

            case 2:
            {
                ChooseNextEffectAction(effects3);
                return;
            }

            case 3:
            {
                vesta.ResearchElixir(currentElixir);
            }
        }
    }

    private void ChooseNextEffectAction(ArrayList<VestaElixirEffect> effects)
    {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (VestaElixirEffect effect : effects)
        {
            Vesta_Elixir other = (Vesta_Elixir) currentElixir.makeStatEquivalentCopy();

            other.ApplyEffect(effect);
            group.addToTop(other);
        }

        GameActions.Top.SelectFromPile(vesta.name, 1, group)
        .SetOptions(false, false)
        .AddCallback(this::ChooseNextEffect);
    }

    private static RandomizedList<VestaElixirEffect> GenerateEffectPool(boolean upgraded)
    {
        RandomizedList<VestaElixirEffect> effectPool = new RandomizedList<>();

        effectPool.Add(new VestaElixirEffect_CardDraw(upgraded));
        effectPool.Add(new VestaElixirEffect_Intellect(upgraded));
        effectPool.Add(new VestaElixirEffect_Force(upgraded));
        effectPool.Add(new VestaElixirEffect_Agility(upgraded));
        effectPool.Add(new VestaElixirEffect_OrbSlots(upgraded));
        effectPool.Add(new VestaElixirEffect_TempHP(upgraded));
        effectPool.Add(new VestaElixirEffect_Metallicize(upgraded));
        effectPool.Add(new VestaElixirEffect_Energy(upgraded));

        return effectPool;
    }
}