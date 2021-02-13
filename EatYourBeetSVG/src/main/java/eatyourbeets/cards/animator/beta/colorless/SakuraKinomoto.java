package eatyourbeets.cards.animator.beta.colorless;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.effects.vfx.SmallLaserEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class SakuraKinomoto extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SakuraKinomoto.class).SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Elemental).SetColor(CardColor.COLORLESS);

    public SakuraKinomoto()
    {
        super(DATA);

        Initialize(17, 0, 2);
        SetUpgrade(0, 0, 1);
        SetScaling(1,0,0);

        SetExhaust(true);
        SetSpellcaster();

        SetSynergy(Synergies.CardcaptorSakura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (m != null)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect(e -> GameEffects.Queue.Add(new SmallLaserEffect(player.hb.cX, player.hb.cY,
                    e.hb.cX + MathUtils.random(-0.05F, 0.05F), e.hb.cY + MathUtils.random(-0.05F, 0.05F), Color.PINK)))
            .AddCallback(enemy ->
            {
                AbstractRoom room = AbstractDungeon.getCurrRoom();
                if ((room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
                && GameUtilities.IsFatal(enemy, false)
                && CombatStats.TryActivateLimited(cardID))
                {
                    RewardItem reward = new RewardItem();
                    CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (int i = 0; i < magicNumber; i++)
                    {
                        AbstractCard card = GameUtilities.GetRandomRewardCard(reward, false);
                        reward.cards.add(card);
                        choices.addToBottom(card);
                    }

                    GameActions.Bottom.SelectFromPile(name, 1, choices)
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            AbstractCard card = cards.get(0).makeCopy();
                            GameActions.Top.MakeCard(card, player.masterDeck);
                        }
                    });
                }
            });
            GameActions.Bottom.SFX("ORB_LIGHTNING_PASSIVE");
        }
    }

}