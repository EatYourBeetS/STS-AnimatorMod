package eatyourbeets.cards.animator.series.LogHorizon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Tetora extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tetora.class)
            .SetPower(0, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Tetora()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.General, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && CheckAffinity(Affinity.General);
    }

    @Override
    public ColoredString GetMagicNumberString()
    {
        if (isMagicNumberModified)
        {
            return new ColoredString(magicNumber, magicNumber >= secondaryValue ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR, transparency);
        }
        else
        {
            return new ColoredString(baseMagicNumber, Settings.CREAM_COLOR, transparency);
        }
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.ModifyMagicNumber(this, GetHandAffinity(Affinity.General), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new TetoraPower(p, 1));
    }

    public static class TetoraPower extends AnimatorPower implements OnSynergySubscriber
    {
        private int synergies;

        public TetoraPower(AbstractPlayer owner, int amount)
        {
            super(owner, Tetora.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onSynergy.Unsubscribe(this);
        }

        @Override
        public void OnSynergy(AbstractCard card)
        {
            synergies = (synergies + 1) % 3;

            if (synergies == 0)
            {
                for (int i = 0; i < amount; i++)
                {
                    GameActions.Bottom.GainRandomAffinityPower(1, false, Affinity.Red, Affinity.Green, Affinity.Blue);
                }

                enabled = false;
                flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(synergies, Settings.BLUE_TEXT_COLOR);
        }
    }
}