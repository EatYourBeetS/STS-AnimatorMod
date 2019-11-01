package eatyourbeets.misc.VestaElixirEffects;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.common.ChooseFromPileAction;
import eatyourbeets.cards.animator.Vesta;
import eatyourbeets.cards.animator.Vesta_Elixir;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.Utilities;

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
            Utilities.Logger.warn("[VestaElixirEffects.ctor] cardRandomRNG was null");
            rng = new Random();
        }

        RandomizedList<VestaElixirEffect> effectPool = GenerateEffectPool();

        effects1.add(effectPool.Retrieve(rng));
        effects1.add(effectPool.Retrieve(rng));
        effects1.add(effectPool.Retrieve(rng));

        effects2.add(effectPool.Retrieve(rng));
        effects2.add(effectPool.Retrieve(rng));
        effects2.add(effectPool.Retrieve(rng));

        effects3.add(new VestaElixirEffect_Exhaust(9, effectPool.Retrieve(rng)));
        effects3.add(new VestaElixirEffect_Discard(10, effectPool.Retrieve(rng)));
        effects3.add(new VestaElixirEffect_CompleteFaster(0));
    }

    public static void BeginCreateElixir(Vesta vesta)
    {
        VestaElixirEffects effects = new VestaElixirEffects(vesta);
        ArrayList<AbstractCard> cards = new ArrayList<>();

        cards.add(new Vesta_Elixir());

        effects.ChooseNextEffect(effects, cards);
    }

    private void ChooseNextEffect(Object state, ArrayList<AbstractCard> cards)
    {
        currentElixir = Utilities.SafeCast(cards.get(0), Vesta_Elixir.class);
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

        GameActionsHelper.AddToBottom(new ChooseFromPileAction(1, false, group, this::ChooseNextEffect,
                                                                 this, "", true));
    }

    private static RandomizedList<VestaElixirEffect> GenerateEffectPool()
    {
        RandomizedList<VestaElixirEffect> effectPool = new RandomizedList<>();

        effectPool.Add(new VestaElixirEffect_CardDraw(1));
        effectPool.Add(new VestaElixirEffect_Intellect(2));
        effectPool.Add(new VestaElixirEffect_Force(3));
        effectPool.Add(new VestaElixirEffect_Agility(4));
        effectPool.Add(new VestaElixirEffect_OrbSlots(5));
        effectPool.Add(new VestaElixirEffect_TempHP(6));
        effectPool.Add(new VestaElixirEffect_Metallicize(7));
        effectPool.Add(new VestaElixirEffect_Energy(8));

        return effectPool;
    }
}