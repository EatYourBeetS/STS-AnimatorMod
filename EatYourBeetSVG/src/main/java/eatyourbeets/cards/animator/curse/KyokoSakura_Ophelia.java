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

public class KyokoSakura_Ophelia extends AnimatorCard_Curse implements OnPurgeSubscriber
{
    public static final EYBCardData DATA = Register(KyokoSakura_Ophelia.class)
            .SetCurse(-2, EYBCardTarget.None, true)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public KyokoSakura_Ophelia()
    {
        super(DATA, true);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Red(1);
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
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.DiscardFromHand(name, magicNumber, true).AddCallback(cards -> {
            GameActions.Bottom.Draw(cards.size());
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card.uuid.equals(this.uuid)) {
            GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
            CombatStats.onPurge.Unsubscribe(this);
        }
    }
}
