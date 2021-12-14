package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import static eatyourbeets.resources.GR.Enums.CardTags.ANIMATOR_ETHEREAL;
import static eatyourbeets.resources.GR.Enums.CardTags.ANIMATOR_EXHAUST;

public class HiedaNoAkyuu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiedaNoAkyuu.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage(true);

    public HiedaNoAkyuu()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(1, 0, 0);

        SetExhaust(true);
        SetCostUpgrade(-1);
        SetAffinityRequirement(Affinity.General, 15);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.MoveCards(player.drawPile, player.discardPile)
        .SetDuration(0.01f, false);

        GameActions.Bottom.SelectFromPile(name, magicNumber, player.discardPile)
        .SetMessage(GR.Common.Strings.GridSelection.Give(magicNumber, GR.Tooltips.Innate.title))
        .SetOptions(false, true)
        .AddCallback(cards ->
        {
            boolean canRemoveTags = cards.size() > 0 && CheckAffinity(Affinity.General) && info.TryActivateLimited();
            for (AbstractCard card : cards)
            {
                GameActions.Bottom.ModifyTag(card,ANIMATOR_INNATE,true);
                if (canRemoveTags) {
                    GameActions.Bottom.ModifyTag(card,ANIMATOR_ETHEREAL,false);
                    GameActions.Bottom.ModifyTag(card,ANIMATOR_EXHAUST,false);
                }
            }
        });

        GameActions.Bottom.StackPower(new NoDrawPower(p));
    }
}

