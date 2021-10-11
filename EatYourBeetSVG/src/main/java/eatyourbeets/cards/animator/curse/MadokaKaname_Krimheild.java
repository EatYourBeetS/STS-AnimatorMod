package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.animator.series.MadokaMagica.SayakaMiki;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class MadokaKaname_Krimheild extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(MadokaKaname_Krimheild.class)
            .SetCurse(1, EYBCardTarget.None, true)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public MadokaKaname_Krimheild()
    {
        super(DATA, false);

        Initialize(0, 0, 99, 0);
        SetUpgrade(0, 0, -89, 0);
        SetAutoplay(true);
        SetPurge(true);

        SetAffinity_Water(2);
        SetAffinity_Dark(2);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new MadokaKaname_KrimheildPower(p, magicNumber));
    }

    public class MadokaKaname_KrimheildPower extends AnimatorPower implements OnOrbApplyFocusSubscriber
    {
        public MadokaKaname_KrimheildPower(AbstractPlayer owner, int amount)
        {
            super(owner, MadokaKaname_Krimheild.DATA);
            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onOrbApplyFocus.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onOrbApplyFocus.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            GameActions.Bottom.ReduceStrength(player,magicNumber,false);
            GameActions.Bottom.GainDexterity(-magicNumber);
            GameActions.Bottom.GainFocus(-magicNumber);
            RemovePower();
        }

        @Override
        public void OnApplyFocus(AbstractOrb orb) {
            orb.passiveAmount *= 3;
            orb.evokeAmount *= 3;
        }
    }
}
