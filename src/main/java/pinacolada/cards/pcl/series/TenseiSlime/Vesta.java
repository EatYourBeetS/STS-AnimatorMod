package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.VestaElixirEffects.VestaElixirEffect;
import pinacolada.cards.base.cardeffects.VestaElixirEffects.VestaElixirEffect_CompleteFaster;
import pinacolada.cards.base.cardeffects.VestaElixirEffects.VestaElixirEffects;
import pinacolada.cards.pcl.special.Vesta_Elixir;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class Vesta extends PCLCard
{
    public static final PCLCardData DATA = Register(Vesta.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .SetSeries(CardSeries.TenseiSlime)
            .PostInitialize(data -> data.AddPreview(new Vesta_Elixir(), false));

    public Vesta()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Silver(1);

        SetAffinityRequirement(PCLAffinity.Blue, 6);
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
        PCLActions.Bottom.GainWisdom(secondaryValue);
        PCLActions.Bottom.GainEndurance(secondaryValue);

        //TODO: This could all be done in VestaPower
        VestaElixirEffects.BeginCreateElixir((Vesta) this.makeStatEquivalentCopy(), TrySpendAffinity(PCLAffinity.Blue));
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
        PCLActions.Bottom.StackPower(new VestaPower(player, elixir, timer));
    }

    public static class VestaPower extends PCLPower
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

            PCLActions.Bottom.MakeCardInHand(elixir);
        }

        @Override
        public AbstractPower makeCopy()
        {
            return new VestaPower(owner, elixir, amount);
        }
    }
}