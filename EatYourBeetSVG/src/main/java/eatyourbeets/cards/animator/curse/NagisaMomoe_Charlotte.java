package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.MadokaMagica.NagisaMomoe;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class NagisaMomoe_Charlotte extends AnimatorCard_Curse implements OnPurgeSubscriber
{
    public static final EYBCardData DATA = Register(NagisaMomoe_Charlotte.class)
            .SetCurse(-2, EYBCardTarget.None, true)
            .SetSeries(NagisaMomoe.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public NagisaMomoe_Charlotte()
    {
        super(DATA, true);

        Initialize(0, 0, 3, 10);
        SetUpgrade(0, 0, 1, 2);

        SetAffinity_Star(1);
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
            GameActions.Bottom.ApplyVulnerable(TargetHelper.AllCharacters(),magicNumber);
            for (AbstractCreature c : GameUtilities.GetAllCharacters(true)) {
                GameActions.Bottom.DealDamageAtEndOfTurn(p, c,secondaryValue);
            }
        }
    }


    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card.uuid.equals(this.uuid)) {
            GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
        }
    }
}
