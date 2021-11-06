package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.MadokaMagica.SayakaMiki;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class SayakaMiki_Oktavia extends AnimatorCard_Curse implements OnPurgeSubscriber
{
    public static final EYBCardData DATA = Register(SayakaMiki_Oktavia.class)
            .SetCurse(-2, EYBCardTarget.None, true)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public SayakaMiki_Oktavia()
    {
        super(DATA, true);

        Initialize(0, 0, 2, 0);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1);
        SetAffinity_Dark(2);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onPurge.Subscribe(this);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard) {
            GameActions.Bottom.ApplyFrail(TargetHelper.AllCharacters(),magicNumber);
            GameActions.Bottom.ApplyWeak(TargetHelper.AllCharacters(),magicNumber);
        }
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card.uuid.equals(this.uuid)) {
            GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
            CombatStats.onPurge.Unsubscribe(this);
        }
    }
}
