package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.cards.animator.series.TenseiSlime.Vesta;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class VestaElixirEffects
{
    private final ArrayList<VestaElixirEffect> effects1 = new ArrayList<>();
    private final ArrayList<VestaElixirEffect> effects2 = new ArrayList<>();
    private final ArrayList<VestaElixirEffect> effects3 = new ArrayList<>();
    private final Vesta vesta;

    public Vesta_Elixir currentElixir;

    public VestaElixirEffects(Vesta vesta)
    {
        this.vesta = vesta;

        Random rng = AbstractDungeon.cardRandomRng;
        if (rng == null)
        {
            JavaUtilities.GetLogger(getClass()).warn("cardRandomRNG was null");
            rng = new Random();
        }

        RandomizedList<VestaElixirEffect> effectPool = GenerateEffectPool();

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

    public static void BeginCreateElixir(Vesta vesta)
    {
        VestaElixirEffects effects = new VestaElixirEffects(vesta);
        ArrayList<AbstractCard> cards = new ArrayList<>();

        cards.add(new Vesta_Elixir());

        effects.ChooseNextEffect(cards);
    }

    private void ChooseNextEffect(ArrayList<AbstractCard> cards)
    {
        currentElixir = JavaUtilities.SafeCast(cards.get(0), Vesta_Elixir.class);
        if (currentElixir == null)
        {
            currentElixir = new Vesta_Elixir();
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
                currentElixir.ApplyEffect(new VestaElixirEffect_Purge(11));
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
        .SetMessage(CardRewardScreen.TEXT[1])
        .SetOptions(false, false)
        .AddCallback(this::ChooseNextEffect);
    }

    private static RandomizedList<VestaElixirEffect> GenerateEffectPool()
    {
        RandomizedList<VestaElixirEffect> effectPool = new RandomizedList<>();

        effectPool.Add(new VestaElixirEffect_CardDraw());
        effectPool.Add(new VestaElixirEffect_Intellect());
        effectPool.Add(new VestaElixirEffect_Force());
        effectPool.Add(new VestaElixirEffect_Agility());
        effectPool.Add(new VestaElixirEffect_OrbSlots());
        effectPool.Add(new VestaElixirEffect_TempHP());
        effectPool.Add(new VestaElixirEffect_Metallicize());
        effectPool.Add(new VestaElixirEffect_Energy());

        return effectPool;
    }
}