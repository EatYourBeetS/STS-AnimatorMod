package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.animator.special.Vesta_Elixir;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.VestaElixirEffects.VestaElixirEffect;
import eatyourbeets.misc.VestaElixirEffects.VestaElixirEffect_CompleteFaster;
import eatyourbeets.misc.VestaElixirEffects.VestaElixirEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Vesta extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Vesta.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeries(CardSeries.TenseiSlime)
            .PostInitialize(data -> data.AddPreview(new Vesta_Elixir(), false));

    public Vesta()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Silver(1);

        SetAffinityRequirement(Affinity.Blue, 3);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainIntellect(1, true);
        GameActions.Bottom.GainWillpower(1, true);

        //TODO: This could all be done in VestaPower
        VestaElixirEffects.BeginCreateElixir((Vesta) this.makeStatEquivalentCopy(), TrySpendAffinity(Affinity.Blue));
    }

    public void ResearchElixir(Vesta_Elixir elixir)
    {
        int timer = magicNumber;
        final ArrayList<VestaElixirEffect> effects = new ArrayList<>();
        for (VestaElixirEffect effect : elixir.effects)
        {
            if (effect instanceof VestaElixirEffect_CompleteFaster)
            {
                timer -= 1;
            }
            else
            {
                effects.add(effect);
            }
        }

        elixir.ApplyEffects(effects);
        GameActions.Bottom.StackPower(new VestaPower(player, elixir, timer));
    }

    public static class VestaPower extends AnimatorPower
    {
        private static int counter = 0;
        private final Vesta_Elixir elixir;

        public VestaPower(AbstractCreature owner, Vesta_Elixir elixir, int amount)
        {
            super(owner, Vesta.DATA);

            this.ID += (counter += 1);
            this.elixir = elixir;

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            ReducePower(1);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            GameActions.Bottom.MakeCardInHand(elixir);
        }

        @Override
        public AbstractPower makeCopy()
        {
            return new VestaPower(owner, elixir, amount);
        }
    }
}