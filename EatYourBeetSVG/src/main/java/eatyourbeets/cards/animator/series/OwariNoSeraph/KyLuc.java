package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class KyLuc extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KyLuc.class)
            .SetAttack(2, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public KyLuc()
    {
        super(DATA);

        Initialize(8, 0, 2, 7);
        SetUpgrade(4, 0, 0, -1);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Dark(2, 0, 2);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        for (AbstractCreature c : GameUtilities.GetAllCharacters(true)) {
            amount += c.powers.size() * magicNumber;
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HORIZONTAL)
                .SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).SetColors(Color.RED, Color.LIGHT_GRAY, Color.RED, Color.RED).duration * 0.6f);
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(true,true,true)
        .AddCallback(cards ->
        {
            if (cards.size() > 0 && (cards.get(0).type.equals(CardType.CURSE) || cards.get(0).rarity.equals(CardRarity.UNCOMMON)))
            {
                GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(),magicNumber);
                GameActions.Bottom.StackPower(TargetHelper.Enemies(), PowerHelper.Shackles, magicNumber);
            }
            else {
                GameActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue, AttackEffects.SLASH_VERTICAL);
            }
        });
    }
}