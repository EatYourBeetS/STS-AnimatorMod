package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.EntouJyuu;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

import java.util.ArrayList;

public class Emonzaemon extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register(Emonzaemon.class.getSimpleName(), EYBCardBadge.Special);

    public Emonzaemon()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(4,0);

        SetPiercing(true);
        SetSynergy(Synergies.Katanagatari);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new EntouJyuu(), true);
        }
    }

//    @Override
//    public List<TooltipInfo> getCustomTooltips()
//    {
//        if (cardText.index == 1)
//        {
//            return super.getCustomTooltips();
//        }
//
//        return null;
//    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + MartialArtist.GetScaling());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.SFX("ATTACK_FIRE");
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE).SetOptions(true, true);
        GameActions.Bottom.SFX("ATTACK_FIRE");
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE).SetOptions(true, true);

        if (!EffectHistory.HasActivatedLimited(cardID))
        {
            ArrayList<AbstractCard> cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
            int size = cardsPlayed.size();
            if (size >= 3)
            {
                boolean threeInARow = true;
                for (int i = 1; i <= 3; i++)
                {
                    if (cardsPlayed.get(size - i).type != CardType.ATTACK)
                    {
                        threeInARow = false;
                    }
                }

                if (threeInARow)
                {
                    EffectHistory.TryActivateLimited(cardID);
                    GameActions.Bottom.MakeCardInDrawPile(this, new EntouJyuu()).SetOptions(upgraded, false);
                }
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }
}