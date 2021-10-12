package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.ImpairedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class Evileye extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Evileye.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    public static final int IMPAIRED_MODIFIER = 100;

    public Evileye()
    {
        super(DATA);

        Initialize(0,0, 2, 3);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Blue, 3);
        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++) {
            GameActions.Bottom.EvokeOrb(2, JUtils.Random(player.orbs)).AddCallback(orbs -> {
                for (AbstractOrb o : orbs) {
                    GameActions.Bottom.ChannelOrb(o);
                }
            });
        }
        GameActions.Bottom.StackPower(new ImpairedPower(player, secondaryValue));

        if (info.CanActivateLimited && TrySpendAffinity(Affinity.Blue, Affinity.Dark) && info.TryActivateLimited()) {
            GameActions.Bottom.StackPower(new EvileyePower(p, 1));
        }
    }

    public static class EvileyePower extends AnimatorPower
    {
        public EvileyePower(AbstractCreature owner, int amount)
        {
            super(owner, Evileye.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            ImpairedPower.AddPlayerModifier(IMPAIRED_MODIFIER);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            ImpairedPower.AddPlayerModifier(-IMPAIRED_MODIFIER);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}