package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class MarisaKirisame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MarisaKirisame.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public MarisaKirisame()
    {
        super(DATA);

        Initialize(7, 0, 1, 0);
        SetUpgrade(3, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetSpellcaster();
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Top.MoveCard(c, AbstractDungeon.player.hand, AbstractDungeon.player.drawPile).SetDestination(CardSelection.Top);
            }

            GameActions.Bottom.Add(new RefreshHandLayout());
        });
        if (HasSynergy())
        {
            if (CombatStats.TryActivateSemiLimited(cardID))
            {
                for (int i = 0; i < magicNumber; i++)
                {
                    GameActions.Bottom.ChannelOrb(new Fire(), true);
                }
            }
        }
    }
}

